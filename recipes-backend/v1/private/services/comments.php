<?php

use Psr\Container\ContainerInterface;
use RecipesSeeker\Model\Comment;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

class CommentController {
    public function __construct() {}

    public function get(Request $request, Response $response, $args) {
        $recipeId = $request->getQueryParam('recipe_id');
        $comments = Comment::where('recipe_id', '=', $recipeId)->get()->all();
        $response->getBody()->write(json_encode($comments));
        return $response;
    }

    function post(Request $request, Response $response, $args) {
        $body = $request->getBody();
        $input = json_decode($body);

        $comment = new Comment();
        $comment->content = $input->content;
        $comment->recipe_id = $input->recipeId;
        $comment->last_modification = new DateTime();
        $comment->save();

        $response->getBody()->write($comment->toJson());
        return $response;
    }

    function put(Request $request, Response $response, $args) {
        $id = $args['id'];
        $body = $request->getBody();
        $input = json_decode($body);
        $comment = Comment::findOrFail($id);
        $comment->content = $input->content;
        $comment->last_modification = new DateTime();
        $comment->save();

        $response->getBody()->write(json_encode(Comment::findOrFail($id)));
    }

    function delete(Request $request, Response $response, $args) {
        $id = $args['id'];
        $comment = Comment::findOrFail($id);
        $comment->delete();
    }
}