import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SettingsService } from '../../services/settings.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {
  activeTab = 'profile';

  setTab(tab: string) {
    this.activeTab = tab;
  }

  user: any = {
    fullName: '',
    email: '',
    bio: '',
    avatarColour: '#2563EB',
  };

  colors = ['#2563EB', '#9333EA', '#16A34A', '#DC2626', '#F97316', '#0EA5E9', '#EC4899'];

  constructor(private service: SettingsService) {}


    ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.service.getProfile().subscribe({
      next: (res) => (this.user = res),
      error: () => alert('Failed to load profile'),
    });
  }

  saveProfile() {
    this.service.updateProfile(this.user).subscribe({
      next: () => {
        alert('Profile updated');

        // 🔥 update navbar instantly
        localStorage.setItem('name', this.user.fullName);
        localStorage.setItem('avatarColour', this.user.avatarColour);

        window.dispatchEvent(new Event('storage'));
      },
      error: () => alert('Update failed'),
    });
  }
}
