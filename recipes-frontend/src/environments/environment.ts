export const environment = {
  production: false,
  // Keep this configurable because the legacy app currently references /backend/v1/public,
  // while the repository backend folder is v2.
  apiBaseUrl: 'https://recipes.clavpa.ch/backend/v2/public'
};
