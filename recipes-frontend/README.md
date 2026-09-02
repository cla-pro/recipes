# Recipe Seeker Frontend Migration (Angular 22)

This folder contains a side-by-side Angular 22 + TypeScript migration workspace.
The legacy AngularJS app remains untouched in the sibling app directory.

## Quick start

### Prerequisites

- Node.js must satisfy Angular CLI requirements.
- Valid versions include:
  - >= 22.22.3
  - >= 24.15.0
  - >= 26.0.0

1. Open a terminal in this folder.
2. Install dependencies:

```bash
npm install
```

If your npm installation reports peer dependency resolution errors, use:

```bash
npm install --legacy-peer-deps
```

3. Start development server:

```bash
npm start
```

4. Open http://localhost:4200

## Backend URL

Default API base URL is configured in src/environments/environment.ts:

- /backend/v2/public

If your deployed API endpoint is still /backend/v1/public, update the environment files.

## Implemented migration scope

- AngularJS routes migrated to Angular Router:
  - /search
  - /search/:id
  - /insert
  - /search/:id/edit
- Restangular calls migrated to HttpClient services.
- Core recipe CRUD flow and comments loading are in place.
- Project is strict TypeScript and standalone-component based.

## Remaining parity tasks

- Move reusable widgets from legacy shared directory:
  - star rating widget
  - comment editor interactions (inline update/delete UX)
  - action/header panel parity
- Add markdown rendering and PDF inline preview if still required.
- Replace browser prompt used for delete confirmation with a proper modal dialog.
- Add auth and error interceptors if backend requires session headers.
- Add unit and integration tests for all services/components.
