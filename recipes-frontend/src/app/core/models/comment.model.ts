export interface RecipeComment {
  id: number;
  content: string;
  recipeId: number;
  creationDateTime?: unknown;
}

export interface UpsertCommentRequest {
  id?: number;
  content: string;
  recipeId: number;
}
