export interface Recipe {
  id: number;
  name: string;
  tags: string[];
  filename?: string;
}

export interface RecipeSearchRequest {
  filter: string;
  size: number;
  chunkStart?: number;
}

export interface UpsertRecipeRequest {
  id?: number;
  name: string;
  filename?: string;
  tags: string[];
}
