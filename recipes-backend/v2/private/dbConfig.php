<?php

use Illuminate\Database\Capsule\Manager as Capsule;

$capsule = new Capsule();
$capsule->addConnection([
    'driver' => getenv('DB_CONNECTION') ?: 'mysql',
    'host' => getenv('DB_HOST') ?: '127.0.0.1',
    'port' => (int)(getenv('DB_PORT') ?: 3306),
    'database' => getenv('DB_DATABASE') ?: 'recipes',
    'username' => getenv('DB_USERNAME') ?: 'root',
    'password' => getenv('DB_PASSWORD') ?: '',
    'charset' => getenv('DB_CHARSET') ?: 'utf8mb4',
    'collation' => getenv('DB_COLLATION') ?: 'utf8mb4_unicode_ci',
    'prefix' => getenv('DB_PREFIX') ?: '',
]);

$capsule->setAsGlobal();
$capsule->bootEloquent();
