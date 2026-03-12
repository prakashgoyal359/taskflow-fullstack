import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appHasRole]',
  standalone: true,
})
export class HasRoleDirective {
  @Input() set appHasRole(roles: string[]) {
    const userRole = localStorage.getItem('role');

    if (roles.includes(userRole || '')) {
      this.vcr.createEmbeddedView(this.tpl);
    } else {
      this.vcr.clear();
    }
  }

  constructor(
    private tpl: TemplateRef<any>,
    private vcr: ViewContainerRef,
  ) {}
}
