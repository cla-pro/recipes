export interface Recipe {
  id: number;
  name: string;
  rating: number;
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
  rating: number;
  filename?: string;
  tags: string[];
}
