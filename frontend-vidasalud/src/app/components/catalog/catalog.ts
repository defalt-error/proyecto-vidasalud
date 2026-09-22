import { Component, OnInit, Input, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CatalogServiceFrontend, ServiceCatalogDto, BoxClinicalDto } from '../../services/catalog.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalog.html',
  styleUrl: './catalog.css'
})
export class CatalogComponent implements OnInit {
  @Input() userRole: string = 'Admin';

  activeTab: 'prestaciones' | 'boxes' = 'prestaciones';
  isLoading: boolean = false;
  successMsg: string = '';
  errorMsg: string = '';

  services: ServiceCatalogDto[] = [];
  boxes: BoxClinicalDto[] = [];

  // Modales y estados de formularios
  showCreateModal: boolean = false;
  showEditModal: boolean = false;
  showBoxModal: boolean = false;

  // Formulario Nueva Prestación
  newService: Partial<ServiceCatalogDto> = {
    code: '',
    name: '',
    category: 'Medicina General',
    description: '',
    price: 20000,
    availableQuota: 10
  };

  // Formulario Edición Prestación
  editingService: Partial<ServiceCatalogDto> = {};

  // Formulario Nuevo Box
  newBox: Partial<BoxClinicalDto> = {
    code: '',
    name: '',
    centerId: 'CENTRO-01',
    specialty: 'Medicina General'
  };

  constructor(
    private catalogService: CatalogServiceFrontend,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCatalog();
  }

  loadCatalog(): void {
    this.isLoading = true;
    this.catalogService.getServices().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.services = data;
        }
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando prestaciones:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });

    this.catalogService.getBoxes().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.boxes = data;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando boxes:', err);
        this.cdr.detectChanges();
      }
    });
  }

  openCreateModal(): void {
    this.newService = {
      code: 'SERV-' + Math.floor(100 + Math.random() * 900),
      name: '',
      category: 'Medicina General',
      description: '',
      price: 25000,
      availableQuota: 10
    };
    this.showCreateModal = true;
    this.cdr.detectChanges();
  }

  saveNewService(): void {
    if (!this.newService.name || !this.newService.code) {
      this.errorMsg = 'El código y nombre de la prestación son obligatorios.';
      this.cdr.detectChanges();
      return;
    }

    this.isLoading = true;
    const createdItem: ServiceCatalogDto = {
      id: Date.now(),
      code: this.newService.code || 'SERV-01',
      name: this.newService.name || 'Nueva Prestación',
      category: this.newService.category || 'Medicina General',
      description: this.newService.description,
      price: Number(this.newService.price) || 20000,
      availableQuota: Number(this.newService.availableQuota) || 10,
      active: true
    };

    this.services.unshift(createdItem);
    this.showCreateModal = false;
    this.showSuccess('Prestación registrada correctamente.');
    this.isLoading = false;
    this.cdr.detectChanges();

    this.catalogService.createService(this.newService).subscribe({
      next: (res) => {
        if (res && res.id) {
          createdItem.id = res.id;
        }
      },
      error: (err) => {
        console.warn('Guardado localmente tras respuesta:', err);
      }
    });
  }

  openEditModal(service: ServiceCatalogDto): void {
    this.editingService = { ...service };
    this.showEditModal = true;
    this.cdr.detectChanges();
  }

  saveEditService(): void {
    if (!this.editingService.id) return;

    this.isLoading = true;
    const index = this.services.findIndex(s => String(s.id) === String(this.editingService.id));
    if (index !== -1) {
      this.services[index] = { ...this.services[index], ...this.editingService };
    }
    this.showEditModal = false;
    this.showSuccess('Prestación actualizada.');
    this.isLoading = false;
    this.cdr.detectChanges();

    this.catalogService.updateService(this.editingService.id, this.editingService).subscribe({
      next: () => {},
      error: () => {}
    });
  }

  deleteService(service: ServiceCatalogDto): void {
    if (!this.userRole.includes('Admin')) {
      alert(`Acceso Denegado: Tu rol activo es "${this.userRole}". La eliminación de prestaciones requiere el rol "Admin". Selecciona el rol Admin en el menú superior.`);
      return;
    }

    if (!confirm(`¿Estás seguro de eliminar/desactivar la prestación "${service.name}"?`)) {
      return;
    }

    this.isLoading = true;

    // Eliminar de la lista local inmediatamente
    this.services = this.services.filter(s => 
      String(s.id) !== String(service.id) && 
      s.code !== service.code && 
      s.name !== service.name
    );

    this.showSuccess('Prestación eliminada/desactivada.');
    this.isLoading = false;
    this.cdr.detectChanges();

    this.catalogService.deleteService(service.id).subscribe({
      next: () => {},
      error: (err) => {
        console.warn('Eliminado localmente tras respuesta:', err);
      }
    });
  }

  openBoxModal(): void {
    this.newBox = {
      code: 'BOX-' + Math.floor(100 + Math.random() * 900),
      name: '',
      centerId: 'CENTRO-SANTIAGO-CENTRO',
      specialty: 'Medicina General'
    };
    this.showBoxModal = true;
    this.cdr.detectChanges();
  }

  saveNewBox(): void {
    if (!this.newBox.name) return;

    const createdBox: BoxClinicalDto = {
      id: Date.now(),
      code: this.newBox.code || 'BOX-NEW',
      name: this.newBox.name || 'Nuevo Box',
      centerId: this.newBox.centerId,
      specialty: this.newBox.specialty,
      active: true
    };

    this.boxes.push(createdBox);
    this.showBoxModal = false;
    this.showSuccess('Box registrado.');
    this.cdr.detectChanges();

    this.catalogService.createBox(this.newBox).subscribe({
      next: () => {},
      error: () => {}
    });
  }

  private showSuccess(msg: string): void {
    this.successMsg = msg;
    this.errorMsg = '';
    this.cdr.detectChanges();
    setTimeout(() => {
      this.successMsg = '';
      this.cdr.detectChanges();
    }, 4000);
  }
}
