import { Routes } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';

export const routes: Routes = [
  {
    path: 'dashboard',
    canActivate: [MsalGuard],
    children: []
  },
  {
    path: 'appointments',
    canActivate: [MsalGuard],
    children: []
  },
  {
    path: 'catalog',
    canActivate: [MsalGuard],
    children: []
  },
  {
    path: 'reports',
    canActivate: [MsalGuard],
    children: []
  },
  {
    path: 'audit',
    canActivate: [MsalGuard],
    children: []
  }
];
