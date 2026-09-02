import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';

import { RecipeComment } from '../../core/models/comment.model';
import { Recipe } from '../../core/models/recipe.model';
import { CommentsApiService } from '../../core/services/comments-api.service';
import { RecipesApiService } from '../../core/services/recipes-api.service';

@Component({
  selector: 'app-recipe-detail-page',
  imports: [CommonModule, RouterLink, NgxExtendedPdfViewerModule],
  templateUrl: './recipe-detail-page.component.html',
  styleUrl: './recipe-detail-page.component.css',
})
export class RecipeDetailPageComponent implements OnInit {
  recipe: Recipe | null = null;
  comments: RecipeComment[] = [];
  loading = true;
  message = '';
  query = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly recipesApi: RecipesApiService,
    private readonly commentsApi: CommentsApiService,
    private readonly ngZone: NgZone,
    private readonly cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.query = this.route.snapshot.queryParamMap.get('query') ?? '';

    if (!idParam) {
      this.router.navigate(['/search']);
      return;
    }

    const recipeId = Number(idParam);
    this.loadRecipe(recipeId);
  }

  recipeFileUrl(): string {
    if (!this.recipe) {
      return '#';
    }
    return this.recipesApi.buildRecipeFileUrl(this.recipe.id);
  }

  recipePdfUrl(): string {
    if (!this.recipe) {
      return '#';
    }
    return this.recipesApi.buildRecipePdfUrl(this.recipe.id);
  }

  private loadRecipe(recipeId: number): void {
    this.loading = true;
    this.recipesApi.getRecipe(recipeId).subscribe({
      next: (recipe) => {
        this.ngZone.run(() => {
          this.recipe = recipe;
          this.commentsApi.listByRecipe(recipe.id).subscribe({
            next: (comments) => {
              this.ngZone.run(() => {
                this.comments = comments;
              });
            },
            error: () => {
              this.ngZone.run(() => {
                this.message = 'Impossible de charger les commentaires';
                this.loading = false;
                this.cdr.detectChanges();
              });
            },
            complete: () => {
              this.ngZone.run(() => {
                this.loading = false;
                this.cdr.detectChanges();
              });
            }
          });
        });
      },
      error: () => {
        this.ngZone.run(() => {
          this.message = 'Recette introuvable';
          this.loading = false;
          this.cdr.detectChanges();
        });
      },
      complete: () => {}
    });
  }
}
