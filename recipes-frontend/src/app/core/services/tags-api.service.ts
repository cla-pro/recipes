import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

import { API_BASE_URL } from '../tokens/api-base-url.token';

interface TagDto {
  name: string;
}

@Injectable({ providedIn: 'root' })
export class TagsApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) private readonly apiBaseUrl: string
  ) {}

  listTagNames(): Observable<string[]> {
    return this.http
      .get<TagDto[]>(`${this.apiBaseUrl}/tags`)
      .pipe(map((tags) => tags.map((tag) => tag.name)));
  }
}
