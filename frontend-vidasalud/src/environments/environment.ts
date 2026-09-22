export const environment = {
  production: false,
  azure: {
    clientId: '1ca49127-cd48-486c-8756-faaef25bb5cf', // Client ID de frontend-vidasalud
    tenantId: 'eae84345-1109-4542-8f5d-b7d04b718034',           // Tenant ID de tu directorio
    authority: 'https://login.microsoftonline.com/eae84345-1109-4542-8f5d-b7d04b718034',
    redirectUri: 'http://localhost:4200',
    scopes: ['api://dc14e643-0c78-4e26-96ea-8ca8c601ad3e/OT.Create'] // Scope de ms-vidasalud-backend
  },
  apiGatewayUrl: 'https://kr4o4wric2.execute-api.us-east-1.amazonaws.com'
};
