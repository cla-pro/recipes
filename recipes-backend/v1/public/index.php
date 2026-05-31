<?php

use RecipesSeeker\Model\Recipe;
use RecipesSeeker\Model\Tag;
use RecipesSeeker\Model\Comment;
use Slim\Factory\AppFactory;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require __DIR__ . '/../vendor/autoload.php';
require __DIR__ . '/../private/private.php';

$app = AppFactory::create();
$app->setBasePath('/backend/v2/public');
$app->addBodyParsingMiddleware();
$app->addRoutingMiddleware();
$app->addErrorMiddleware(true, true, true);

$commentController = new CommentController();
$tagController = new TagController();
$recipeController = new RecipeController();

$app->get(
    '/',
    function (Request $request, Response $response) {
        $response->getBody()->write("Welcome to API -> PHP Version " . phpversion());
        return $response;
    }
);

// TODO use groups
$app->get('/recipes', [$recipeController, 'getWithFilter']);
$app->get('/recipes/{id}', [$recipeController, 'getById']);
$app->get('/recipes/pdf/{id}', [$recipeController, 'getPDF']);
$app->get('/recipes/file/{id}', [$recipeController, 'getSourceFile']);
$app->post('/recipes', [$recipeController, 'createRecipe']);
$app->put('/recipes/{id}', [$recipeController, 'updateRecipe']);
$app->post('/recipes/{id}/file', [$recipeController, 'updateRecipeFile']);

// TODO use groups
$app->get('/comments', [$commentController, 'get']);
$app->post('/comments', [$commentController, 'post']);
$app->put('/comments/{id}', [$commentController, 'put']);
$app->delete('/comments/{id}', [$commentController, 'delete']);

// TODO use groups
$app->get('/tags', [$tagController, 'get']);

$app->run();
