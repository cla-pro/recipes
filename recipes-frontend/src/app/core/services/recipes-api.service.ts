import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Recipe, RecipeSearchRequest, UpsertRecipeRequest } from '../models/recipe.model';
import { API_BASE_URL } from '../tokens/api-base-url.token';

@Injectable({ providedIn: 'root' })
export class RecipesApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) private readonly apiBaseUrl: string
  ) {}

  searchRecipes(request: RecipeSearchRequest): Observable<Recipe[]> {
    let params = new HttpParams()
      .set('filter', request.filter)
      .set('size', String(request.size));

    if (request.chunkStart !== undefined) {
      params = params.set('chunkStart', String(request.chunkStart));
    }

    return this.http.get<Recipe[]>(`${this.apiBaseUrl}/recipes`, { params });
  }

  getRecipe(id: number): Observable<Recipe> {
    return this.http.get<Recipe>(`${this.apiBaseUrl}/recipes/${id}`);
  }

  createRecipe(request: UpsertRecipeRequest, file: File): Observable<Recipe> {
    const formData = new FormData();
    formData.append('recipe', JSON.stringify(request));
    formData.append('file', file);
    return this.http.post<Recipe>(`${this.apiBaseUrl}/recipes`, formData);
  }

  updateRecipe(request: UpsertRecipeRequest): Observable<Recipe> {
    return this.http.put<Recipe>(`${this.apiBaseUrl}/recipes/${request.id}`, request);
  }

  uploadRecipeFile(recipeId: number, file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<void>(`${this.apiBaseUrl}/recipes/${recipeId}/file`, formData);
  }

  deleteRecipe(recipeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/recipes/${recipeId}`);
  }

  buildRecipeFileUrl(recipeId: number): string {
    return `${this.apiBaseUrl}/recipes/file/${recipeId}`;
  }

  buildRecipePdfUrl(recipeId: number): string {
    return `${this.apiBaseUrl}/recipes/pdf/${recipeId}`;
  }
}
