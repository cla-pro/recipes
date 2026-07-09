<?php namespace RecipesSeeker\Model;

use Illuminate\Database\Eloquent\Model;

class Tag extends Model {

    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'tag';

    public $timestamps = false;

    // 'id', 'created_at' and 'updated_at' columns are automatically added by Eloquent
}
