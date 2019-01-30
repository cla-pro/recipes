<?php

use Psr\Container\ContainerInterface;
use RecipesSeeker\Model\Recipe;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

class RecipeController {
    const RECIPES_FOLDER = "/homez.259/clavpacheb/recipes/";

    public function __construct() {}

    function getById(Request $request, Response $response, $args) {
        $id = $args['id'];
        $recipe = Recipe::find($id);
        $object = $this->extractRecipeInfo($recipe);
        $response->getBody()->write(json_encode($object));
        return $response;
    }

    function getWithFilter(Request $request, Response $response, $args) {
        $filter = $request->getQueryParam('filter');
        $chunkStart = $request->getQueryParam('chunkStart'); // id of the last received recipe
        $size = $request->getQueryParam('size');

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

    function getPDF(Request $request, Response $response, $args) {
        $id = $args['id'];
        $recipe = Recipe::find($id);
        $file = self::RECIPES_FOLDER . $this->toPdfFilename($recipe->filename);

        if (file_exists($file)) {
            $fh = fopen($file, 'rb');
            $stream = new \Slim\Http\Stream($fh);
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

    function getSourceFile(Request $request, Response $response, $args) {
        $id = $args['id'];
        $recipe = Recipe::find($id);
        $file = self::RECIPES_FOLDER . $recipe->filename;

        if (file_exists($file)) {
            $fh = fopen($file, 'rb');
            $stream = new \Slim\Http\Stream($fh);
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
            "rating" => $dbRecipe->reting,
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
