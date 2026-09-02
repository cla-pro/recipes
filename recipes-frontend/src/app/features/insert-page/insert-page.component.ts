import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { TagsApiService } from '../../core/services/tags-api.service';
import { RecipesApiService } from '../../core/services/recipes-api.service';

interface InsertForm {
  name: FormControl<string>;
  tags: FormControl<string>;
  rating: FormControl<number>;
}

@Component({
  selector: 'app-insert-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './insert-page.component.html',
  styleUrl: './insert-page.component.css',
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class InsertPageComponent {
  readonly form = new FormGroup<InsertForm>({
    name: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    tags: new FormControl('', { nonNullable: true }),
    rating: new FormControl(0, { nonNullable: true })
  });

  selectedFile: File | null = null;
  allTags: string[] = [];
  loading = false;
  message = '';
  isError = false;

  constructor(
    private readonly recipesApi: RecipesApiService,
    private readonly tagsApi: TagsApiService
  ) {
    this.tagsApi.listTagNames().subscribe({
      next: (tags) => {
        this.allTags = tags;
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files && input.files.length > 0 ? input.files[0] : null;
    if (!this.form.controls.name.value && this.selectedFile) {
      const filename = this.selectedFile.name.replace(/\.[^.]+$/, '').replace(/_/g, ' ');
      this.form.controls.name.setValue(filename);
    }
  }

  save(): void {
    this.markMessage('', false);

    if (this.form.invalid || !this.selectedFile) {
      this.markMessage('Le nom de la recette et le fichier sont obligatoires', true);
      return;
    }

    const request = {
      name: this.form.controls.name.value.trim(),
      rating: this.form.controls.rating.value,
      filename: this.selectedFile.name,
      tags: this.parseTags(this.form.controls.tags.value)
    };

    this.loading = true;
    this.recipesApi.createRecipe(request, this.selectedFile).subscribe({
      next: () => {
        this.form.reset({ name: '', tags: '', rating: 0 });
        this.selectedFile = null;
        this.markMessage('Recette enregistree', false);
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
    return 'Une erreur est survenue pendant l enregistrement';
  }

  private markMessage(message: string, isError: boolean): void {
    this.message = message;
    this.isError = isError;
  }
}
