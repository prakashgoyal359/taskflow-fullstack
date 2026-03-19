import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SettingsService } from '../services/settings.service';
import { ProfileComponent } from './profile/profile.component';
@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule, ProfileComponent],
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  // active tab (for future use)
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

  //   Theme code start

  // ✅ theme state
  currentTheme = localStorage.getItem('taskflow_theme') || 'LIGHT';

  setTheme(theme: string) {
    this.currentTheme = theme;

    localStorage.setItem('taskflow_theme', theme);

    // APPLY THEME GLOBALLY
    if (theme === 'DARK') {
      document.documentElement.classList.add('dark');
    } else if (theme === 'LIGHT') {
      document.documentElement.classList.remove('dark');
    } else {
      // SYSTEM
      const dark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      document.documentElement.classList.toggle('dark', dark);
    }
  }

  // Theme code end

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
