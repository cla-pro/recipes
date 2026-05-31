<?php

use RecipesSeeker\Model\Comment;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

class CommentController {
    public function __construct() {}

    public function get(Request $request, Response $response, array $args = []) {
        $queryParams = $request->getQueryParams();
        $recipeId = $queryParams['recipe_id'] ?? null;
        $comments = Comment::where('recipe_id', '=', $recipeId)->get()->all();
        $response->getBody()->write(json_encode($comments));
        return $response;
    }

    public function post(Request $request, Response $response, array $args = []) {
        $input = json_decode((string)$request->getBody());

        $comment = new Comment();
        $comment->content = $input->content;
        $comment->recipe_id = $input->recipeId;
        $comment->last_modification = new DateTime();
        $comment->save();

        $response->getBody()->write($comment->toJson());
        return $response;
    }

    public function put(Request $request, Response $response, array $args = []) {
        $id = $args['id'];
        $input = json_decode((string)$request->getBody());
        $comment = Comment::findOrFail($id);
        $comment->content = $input->content;
        $comment->last_modification = new DateTime();
        $comment->save();

        $response->getBody()->write(json_encode(Comment::findOrFail($id)));
        return $response;
    }

    public function delete(Request $request, Response $response, array $args = []) {
        $id = $args['id'];
        $comment = Comment::findOrFail($id);
        $comment->delete();
        return $response;
    }
}