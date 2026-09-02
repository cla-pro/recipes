import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { Recipe } from '../../core/models/recipe.model';
import { RecipesApiService } from '../../core/services/recipes-api.service';

@Component({
  selector: 'app-search-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css'
})
export class SearchPageComponent implements OnInit {
  readonly queryControl = new FormControl('', { nonNullable: true });

  recipes: Recipe[] = [];
  hasMoreRecipes = false;
  loading = false;
  message = '';

  private readonly pageSize = 50;
  
  constructor(
    private readonly recipesApi: RecipesApiService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly ngZone: NgZone,
    private readonly cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      const query = params.get('query') ?? '';
      this.queryControl.setValue(query, { emitEvent: false });
      if (query.length > 0) {
        this.runSearch(true);
      } else {
        this.recipes = [];
        this.message = '';
      }
    });
  }

  submitSearch(): void {
    this.router.navigate(['/search'], {
      queryParams: { query: this.queryControl.value || null }
    });
  }

  loadMore(): void {
    this.runSearch();
  }

  private runSearch(reset: boolean = false): void {
    const query = this.queryControl.value.trim();

    this.loading = true;
    this.message = '';
    if (reset) {
      this.recipes = [];
    }

    const chunkStart = this.recipes.length > 0 ? this.recipes[this.recipes.length - 1].id : undefined;

    this.recipesApi
      .searchRecipes({
        filter: query,
        size: this.pageSize,
        chunkStart
      })
      .subscribe({
        next: (found) => {
          this.ngZone.run(() => {
            this.recipes = [...this.recipes, ...found];
            this.hasMoreRecipes = found.length === this.pageSize;
            if (this.recipes.length === 0) {
              this.message = 'Pas de recette trouvee';
            }
            this.cdr.detectChanges();
          });
        },
        error: (err: unknown) => {
          this.ngZone.run(() => {
            this.message = this.getErrorMessage(err);
            this.loading = false;
            this.cdr.detectChanges();
          });
        },
        complete: () => {
          this.ngZone.run(() => {
            this.loading = false;
            console.log('Search completed ', this.loading, ' ', this.recipes.map((r) => r.name).join(', '));
            this.cdr.detectChanges();
          });
        }
      });
  }

  private getErrorMessage(err: unknown): string {
    if (typeof err === 'object' && err !== null && 'error' in err) {
      const errorObj = (err as { error?: { message?: string } }).error;
      if (errorObj?.message) {
        return errorObj.message;
      }
    }
    return 'Erreur lors de la recherche';
  }
}
