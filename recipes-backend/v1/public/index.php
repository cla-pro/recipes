<?php

use Mailgun\Mailgun;

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require __DIR__ . '/../private/private.php';
require __DIR__ . '/../vendor/autoload.php';

$app = new \Slim\App(['settings' => ['determineRouteBeforeAppMiddleware' => true, 'addContentLengthHeader' => false]]);

$mailgun = Mailgun::create($GLOBALS['KEY_MAILGUN']);

$app->get(
    '/',
    function () {
        echo "Welcome to API -> PHP Version " . phpversion();
    }
);

$app->post(
    '/sendOrder',
    function(Request $request, Response $response) use ($mailgun) {
        $input = $request->getParsedBody();

        $mustache = new Mustache_Engine(array('loader' => new Mustache_Loader_FilesystemLoader(dirname(__FILE__).'/templates')));
        $template = $mustache->loadTemplate('/order_mail.mustache');
        $order = $template->render($input['shoppingCart']);

        $message = [
            'from'    => 'order@supunsa.ch',
            'to'      => ['cedric.lavanchy@gmail.com', $input['email']],
            'subject' => 'Order',
            'html'    => $order
        ];
        $mailgun->messages()->send('sandboxa75b806dd5634524bb28f6247a91dd7d.mailgun.org', $message);
    }
);

$app->run();
