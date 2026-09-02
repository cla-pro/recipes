import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { switchMap } from 'rxjs';

import { Recipe } from '../../core/models/recipe.model';
import { RecipesApiService } from '../../core/services/recipes-api.service';

interface EditForm {
  name: FormControl<string>;
  tags: FormControl<string>;
  rating: FormControl<number>;
}

@Component({
  selector: 'app-edit-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.css',
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class EditPageComponent implements OnInit {
  readonly form = new FormGroup<EditForm>({
    name: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    tags: new FormControl('', { nonNullable: true }),
    rating: new FormControl(0, { nonNullable: true })
  });

  recipeId = 0;
  query = '';
  selectedFile: File | null = null;
  loading = false;
  deleting = false;
  message = '';
  isError = false;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly recipesApi: RecipesApiService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.query = this.route.snapshot.queryParamMap.get('query') ?? '';

    if (!idParam) {
      this.router.navigate(['/search']);
      return;
    }

    this.recipeId = Number(idParam);
    this.loading = true;
    this.recipesApi.getRecipe(this.recipeId).subscribe({
      next: (recipe) => this.patchForm(recipe),
      error: () => this.markMessage('Impossible de charger la recette', true),
      complete: () => {
        this.loading = false;
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files && input.files.length > 0 ? input.files[0] : null;
  }

  save(): void {
    this.markMessage('', false);
    if (this.form.invalid) {
      this.markMessage('Le nom de la recette est obligatoire', true);
      return;
    }

    this.loading = true;
    this.recipesApi
      .updateRecipe({
        id: this.recipeId,
        name: this.form.controls.name.value.trim(),
        rating: this.form.controls.rating.value,
        tags: this.parseTags(this.form.controls.tags.value),
        filename: this.selectedFile?.name
      })
      .pipe(
        switchMap(() => {
          if (this.selectedFile) {
            return this.recipesApi.uploadRecipeFile(this.recipeId, this.selectedFile);
          }
          return of(void 0);
        })
      )
      .subscribe({
        next: () => {
          this.router.navigate(['/search', this.recipeId], { queryParams: { query: this.query || null } });
        },
        error: (err: unknown) => {
          this.markMessage(this.getErrorMessage(err), true);
          this.loading = false;
        },
        complete: () => {
          this.loading = false;
        }
      });
  }

  deleteRecipe(): void {
    const password = prompt('Confirmer suppression. Mot de passe:');
    if (password === null) {
      return;
    }
    if (password !== 'secret') {
      this.markMessage('Mot de passe incorrect', true);
      return;
    }

    this.deleting = true;
    this.recipesApi.deleteRecipe(this.recipeId).subscribe({
      next: () => {
        this.router.navigate(['/search'], { queryParams: { query: this.query || null } });
      },
      error: () => {
        this.markMessage('Erreur pendant la suppression', true);
      },
      complete: () => {
        this.deleting = false;
      }
    });
  }

  back(): void {
    this.router.navigate(['/search', this.recipeId], { queryParams: { query: this.query || null } });
  }

  private patchForm(recipe: Recipe): void {
    this.form.patchValue({
      name: recipe.name,
      rating: recipe.rating,
      tags: recipe.tags.join(', ')
    });
  }

  private parseTags(rawTags: string): string[] {
    return rawTags
      .split(',')
      .map((tag) => tag.trim())
      .filter((tag) => tag.length > 0);
  }

  private getErrorMessage(err: unknown): string {
    if (typeof err === 'object' && err !== null && 'error' in err) {
      const errorObj = (err as { error?: { message?: string } }).error;
      if (errorObj?.message) {
        return `Erreur pendant l'enregistrement: ${errorObj.message}`;
      }
    }
    return 'Une erreur est survenue';
  }

  private markMessage(message: string, isError: boolean): void {
    this.message = message;
    this.isError = isError;
  }
}
