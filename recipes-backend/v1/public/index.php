<?php

use RecipesSeeker\Model\Recipe;
use RecipesSeeker\Model\Tag;
use RecipesSeeker\Model\Comment;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require __DIR__ . '/../vendor/autoload.php';
require __DIR__ . '/../private/private.php';

$app = new \Slim\App(['settings' => ['determineRouteBeforeAppMiddleware' => true, 'addContentLengthHeader' => false]]);
$container = $app->getContainer();
$container['CommentController'] = function($c) {
    return new CommentController();
};
$container['TagController'] = function($c) {
    return new TagController();
};
$container['RecipeController'] = function($c) {
    return new RecipeController();
};

$app->get(
    '/',
    function () {
        echo "Welcome to API -> PHP Version " . phpversion();
    }
);

// TODO use groups
$app->get('/recipes', \RecipeController::class . ':getWithFilter');
$app->get('/recipes/{id}', \RecipeController::class . ':getById');
$app->get('/recipes/pdf/{id}', \RecipeController::class . ':getPDF');
$app->get('/recipes/file/{id}', \RecipeController::class . ':getSourceFile');
$app->post('/recipes', \RecipeController::class . ':post');

// TODO use groups
$app->get('/comments', \CommentController::class . ':get');
$app->post('/comments', \CommentController::class . ':post');
$app->put('/comments/{id}', \CommentController::class . ':put');
$app->delete('/comments/{id}', \CommentController::class . ':delete');

// TODO use groups
$app->get('/tags', \TagController::class . ':get');

$app->run();
