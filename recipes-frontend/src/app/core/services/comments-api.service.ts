import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { RecipeComment, UpsertCommentRequest } from '../models/comment.model';
import { API_BASE_URL } from '../tokens/api-base-url.token';

@Injectable({ providedIn: 'root' })
export class CommentsApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) private readonly apiBaseUrl: string
  ) {}

  listByRecipe(recipeId: number): Observable<RecipeComment[]> {
    const params = new HttpParams().set('recipe_id', String(recipeId));
    return this.http.get<RecipeComment[]>(`${this.apiBaseUrl}/comments`, { params });
  }

  createComment(request: UpsertCommentRequest): Observable<RecipeComment> {
    return this.http.post<RecipeComment>(`${this.apiBaseUrl}/comments`, request);
  }

  updateComment(commentId: number, request: UpsertCommentRequest): Observable<RecipeComment> {
    return this.http.put<RecipeComment>(`${this.apiBaseUrl}/comments/${commentId}`, request);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/comments/${commentId}`);
  }
}
