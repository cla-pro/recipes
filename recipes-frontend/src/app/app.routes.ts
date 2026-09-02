import { Routes } from '@angular/router';

import { EditPageComponent } from './features/edit-page/edit-page.component';
import { InsertPageComponent } from './features/insert-page/insert-page.component';
import { RecipeDetailPageComponent } from './features/recipe-detail-page/recipe-detail-page.component';
import { SearchPageComponent } from './features/search-page/search-page.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'search' },
  { path: 'search', component: SearchPageComponent },
  { path: 'search/:id', component: RecipeDetailPageComponent },
  { path: 'search/:id/edit', component: EditPageComponent },
  { path: 'insert', component: InsertPageComponent },
  { path: '**', redirectTo: 'search' }
];
