# Database setup

The `schema.sql` file is the consolidated MySQL/MariaDB schema corresponding to
the Liquibase changelogs previously located in `recipes-db`. The Liquibase
sequence changes are represented by the `AUTO_INCREMENT` primary keys and
therefore are not emitted separately for MySQL/MariaDB.

## Create the schema

Run the script using a privileged database account after creating and selecting
the target database:

```sql
CREATE DATABASE recipes
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE recipes;
SOURCE /path/to/recipes-backend/database/schema.sql;
```

## Configure credentials

The PHP application reads database settings from server environment variables.
Copy `.env.example` as a reference, but store real values in the hosting
platform's secret manager or an environment-specific file outside version
control. For the v2 application, use `v2/.env.example` as the template. Do not
commit `.env` or put credentials in `schema.sql`.

Required variables:

- `DB_HOST`
- `DB_PORT`
- `DB_DATABASE`
- `DB_USERNAME`
- `DB_PASSWORD`

Optional variables with safe non-secret defaults are `DB_CONNECTION`, `DB_CHARSET`,
`DB_COLLATION`, and `DB_PREFIX`.

Use a dedicated application account with only the permissions required at
runtime. The privileged account used to create tables should be separate.
