<?php namespace RecipesSeeker\Model;

use Illuminate\Database\Eloquent\Model;

class Recipe extends Model {

    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'recipe';

    public $timestamps = false;

    // 'id', 'created_at' and 'updated_at' columns are automatically added by Eloquent

    public function tags() {
        return $this->belongsToMany('RecipesSeeker\\Model\\Tag', 'recipe_tag');
    }
}
