<?php

use RecipesSeeker\Model\Recipe;
use RecipesSeeker\Model\Tag;
use Slim\Psr7\Stream;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

class RecipeController {
    const RECIPES_FOLDER = "/homez.259/clavpacheb/recipes/";

    public function __construct() {}

    public function getById(Request $request, Response $response, array $args = []) {
        $id = $args['id'];
        $recipe = Recipe::find($id);
        $object = $this->extractRecipeInfo($recipe);
        $response->getBody()->write(json_encode($object));
        return $response;
    }

    public function getWithFilter(Request $request, Response $response, array $args = []) {
        $queryParams = $request->getQueryParams();
        $filter = $queryParams['filter'] ?? '';
        $chunkStart = $queryParams['chunkStart'] ?? null; // id of the last received recipe
        $size = (int)($queryParams['size'] ?? 20);

        $startName = null;
        if (!is_null($chunkStart)) {
            $start = Recipe::find($chunkStart);
            $startName = $start->name;
        }

        $words = explode(' ', $filter);
        $recipes = $this->appendChunk($this->buildQuery($words), $startName)
                        ->orderBy('name')
                        ->take($size)
                        ->get()
                        ->all();

        $converted = array_map(function($item) { return $this->extractRecipeInfo($item); }, $recipes);

        $response->getBody()->write(json_encode($converted));
        return $response;
    }

    public function getPDF(Request $request, Response $response, array $args = []) {
        $id = $args['id'];
        $recipe = Recipe::find($id);
        $file = self::RECIPES_FOLDER . $this->toPdfFilename($recipe->filename);

        if (file_exists($file)) {
            $fh = fopen($file, 'rb');
            $stream = new Stream($fh);
            return $response->withHeader('Content-Description', 'File Transfer')
                            ->withHeader('Content-Type', 'application/octet-stream')
                            ->withHeader('Content-Disposition', 'attachment;filename="' . basename($file) . '"')
                            ->withHeader('Expires', '0')
                            ->withHeader('Cache-Control', 'must-revalidate')
                            ->withHeader('Pragma', 'public')
                            ->withHeader('Content-Length', filesize($file))
                            ->withBody($stream);
        } else {
            $response->getBody()->write('File ' . $file . ' not found');
            return $response;
        }
    }

    public function getSourceFile(Request $request, Response $response, array $args = []) {
        $id = $args['id'];
        $recipe = Recipe::find($id);
        $file = self::RECIPES_FOLDER . $recipe->filename;

        if (file_exists($file)) {
            $fh = fopen($file, 'rb');
            $stream = new Stream($fh);
            return $response->withHeader('Content-Description', 'File Transfer')
                            ->withHeader('Content-Type', 'application/octet-stream')
                            ->withHeader('Content-Disposition', 'attachment;filename="' . basename($file) . '"')
                            ->withHeader('Expires', '0')
                            ->withHeader('Cache-Control', 'must-revalidate')
                            ->withHeader('Pragma', 'public')
                            ->withHeader('Content-Length', filesize($file))
                            ->withBody($stream);
        } else {
            $response->getBody()->write('File ' . $file . ' not found');
            return $response;
        }
    }

    public function createRecipe(Request $request, Response $response, array $args = []) {
        $parsedBody = $request->getParsedBody();
        $recipePayload = is_array($parsedBody) ? ($parsedBody['recipe'] ?? '{}') : '{}';
        $recipe = json_decode((string)$recipePayload);
        $files = $request->getUploadedFiles();
        $file = $files['file'];
        $extension = pathinfo($file->getClientFilename(), PATHINFO_EXTENSION);
        $filename = $this->fixFilename($recipe->name) . '.' . $extension;

        $fileDestination = self::RECIPES_FOLDER . $filename;
        $file->moveTo($fileDestination);

        if ($extension !== 'pdf') {
            $pdfPath = $this->toPdfFilename($filename);
            $this->imageToPdf($fileDestination, $pdfPath);
        }

        $persisted = $this->persistRecipe($recipe, $filename);

        $response->getBody()->write(json_encode($this->extractRecipeInfo($persisted)));
        return $response;
    }

    public function updateRecipe(Request $request, Response $response, array $args = []) {
        $input = json_decode((string)$request->getBody());
        $tagIds = $this->persistNewTagsAndGetIds($input->tags);

        $id = $args['id'];
        $dbRecipe = Recipe::find($id);
        $dbRecipe->name = $input->name;
        $dbRecipe->tags()->sync($tagIds);
        $dbRecipe->save();

        $object = $this->extractRecipeInfo($dbRecipe);
        $response->getBody()->write(json_encode($object));
        return $response;
    }

    public function updateRecipeFile(Request $request, Response $response, array $args = []) {
        $files = $request->getUploadedFiles();
        $file = $files['file'];
        $extension = pathinfo($file->getClientFilename(), PATHINFO_EXTENSION);

        $id = $args['id'];
        $recipe = Recipe::find($id);
        $filename = $this->fixFilename($recipe->name) . '.' . $extension;

        $fileDestination = self::RECIPES_FOLDER . $filename;
        if (file_exists($fileDestination)) {
            unlink($fileDestination);
        }
        $file->moveTo($fileDestination);

        if ($extension !== 'pdf') {
            $pdfPath = $this->toPdfFilename($filename);
            $this->imageToPdf($fileDestination, $pdfPath);
        }

        $recipe->filename = $filename;
        $recipe->save();

        return $response;
    }

    function persistRecipe($input, $filename) {
        $tagIds = $this->persistNewTagsAndGetIds($input->tags);

        $recipe = new Recipe();
        $recipe->name = $input->name;
        $recipe->filename = $filename;
        $recipe->rating = 0;
        $recipe->save();

        $recipe->tags()->attach($tagIds);
        $recipe->save();

        return $recipe;
    }

    function imageToPDF($imagePath, $pdfPath) {
        $pdf = new FPDF();
        $pdf->AddPage();
        $pdf->Image($imagePath, 0, 0, 210, 297);
        $pdf->Output(self::RECIPES_FOLDER . $pdfPath, 'F');
    }

    function fixFilename($filename) {
        return str_replace(' ', '_', str_replace(',', '', $filename));
    }

    function persistNewTagsAndGetIds($tags) {
        $persistedIds = [];
        foreach ($tags as $t) {
            // TODO check standard keyword

            $dbTag = Tag::where('tag', '=', $t)->first();
            if ($dbTag == null) {
                $newTag = new Tag();
                $newTag->tag = $t;
                $newTag->save();
                array_push($persistedIds, $newTag->id);
            } else {
                array_push($persistedIds, $dbTag->id);
            }
        }

        return $persistedIds;
    }

    function toPdfFilename($filename) {
        $info = pathinfo($filename);
        $pdfExt = 'pdf';
        if ($info['extension'] == $pdfExt) {
            return $filename;
        } else {
            return $info['filename'] . '.' . $pdfExt;
        }
    }

    function extractRecipeInfo($dbRecipe) {
        $tags = array_map(function($item) { return $item->tag; }, $dbRecipe->tags()->get()->all());
        return array(
            "id" => $dbRecipe->id,
            "name" => $dbRecipe->name,
            "filename" => $dbRecipe->filename,
            "rating" => $dbRecipe->rating,
            "tags" => $tags
        );
    }

    function buildQuery($words) {
        $query = null;
        foreach ($words as $word) {
            if ($query == null) {
                $query = Recipe::where('name', 'like', '%' . $word . '%');
            } else {
                $query = $query->where('name', 'like', '%' . $word . '%');
            }
        }

        return $query;
    }

    function appendChunk($query, $chunkStart) {
        if (is_null($chunkStart)) {
            return $query;
        } else {
            return $query->where('name', '>', $chunkStart);
        }
    }
}
