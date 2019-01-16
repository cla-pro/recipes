<?php

use Psr\Container\ContainerInterface;
use RecipesSeeker\Model\Tag;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

class TagController {
    public function __construct() {}

    function get(Request $request, Response $response, $args) {
        $tags = Tag::all()->map(function($t) { return array(
                'id' => $t->id,
                'name' => $t->tag,
                'modificationDate' => $t->modification_date
            );
        });
        $response->getBody()->write(json_encode($tags));
        return $response;
    }
}
