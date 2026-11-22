<?php

use Illuminate\Database\Capsule\Manager as Capsule;

function requireDatabaseEnvironmentVariable(string $name): string
{
    $value = getenv($name);

    if ($value === false || $value === '') {
        throw new RuntimeException("Required database environment variable {$name} is not set.");
    }

    return $value;
}

$capsule = new Capsule();
$capsule->addConnection([
    'driver' => getenv('DB_CONNECTION') ?: 'mysql',
    'host' => requireDatabaseEnvironmentVariable('DB_HOST'),
    'port' => (int)requireDatabaseEnvironmentVariable('DB_PORT'),
    'database' => requireDatabaseEnvironmentVariable('DB_DATABASE'),
    'username' => requireDatabaseEnvironmentVariable('DB_USERNAME'),
    'password' => requireDatabaseEnvironmentVariable('DB_PASSWORD'),
    'charset' => getenv('DB_CHARSET') ?: 'utf8mb4',
    'collation' => getenv('DB_COLLATION') ?: 'utf8mb4_unicode_ci',
    'prefix' => getenv('DB_PREFIX') ?: '',
]);

$capsule->setAsGlobal();
$capsule->bootEloquent();
