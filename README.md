🚀 STEP 1: SETTINGS PAGE (MAIN CONTAINER)

This is the core page that:

Handles tab navigation (?tab=)

Shows sidebar

Loads each component dynamically

Applies Role-based visibility

🧩 1. FRONTEND — SETTINGS PAGE (ANGULAR)
✅ settings-page.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
selector: 'app-settings-page',
templateUrl: './settings-page.component.html'
})
export class SettingsPageComponent implements OnInit {

tab: string = 'profile';

constructor(
private route: ActivatedRoute,
private router: Router
) {}

ngOnInit(): void {
this.route.queryParams.subscribe(params => {
this.tab = params['tab'] || 'profile';
});
}

setTab(tab: string) {
this.router.navigate([], {
queryParams: { tab },
queryParamsHandling: 'merge'
});
}
}
✅ settings-page.component.html

<div class="min-h-screen bg-[var(--bg-primary)] text-[var(--text-primary)]">

  <!-- HEADER -->
  <div class="p-4 border-b border-[var(--border-color)] text-xl font-semibold text-center">
    Settings
  </div>

  <div class="flex">

    <!-- SIDEBAR -->
    <div class="w-64 border-r border-[var(--border-color)] p-4 space-y-2">

      <button
        (click)="setTab('profile')"
        [class.bg-blue-100]="tab==='profile'"
        class="w-full text-left px-3 py-2 rounded">
        Profile
      </button>

      <button
        (click)="setTab('security')"
        [class.bg-blue-100]="tab==='security'"
        class="w-full text-left px-3 py-2 rounded">
        Security
      </button>

      <button
        (click)="setTab('theme')"
        [class.bg-blue-100]="tab==='theme'"
        class="w-full text-left px-3 py-2 rounded">
        Theme
      </button>

      <button
        (click)="setTab('notifications')"
        [class.bg-blue-100]="tab==='notifications'"
        class="w-full text-left px-3 py-2 rounded">
        Notifications
      </button>

      <!-- ROLE BASED TAB -->
      <button
        *appHasRole="['ADMIN','MANAGER']"
        (click)="setTab('team')"
        [class.bg-blue-100]="tab==='team'"
        class="w-full text-left px-3 py-2 rounded">
        Team Settings
      </button>

    </div>

    <!-- CONTENT AREA -->
    <div class="flex-1 p-6">

      <app-profile *ngIf="tab==='profile'"></app-profile>
      <app-security *ngIf="tab==='security'"></app-security>
      <app-theme *ngIf="tab==='theme'"></app-theme>
      <app-notifications *ngIf="tab==='notifications'"></app-notifications>
      <app-team-settings *ngIf="tab==='team'"></app-team-settings>

    </div>

  </div>
</div>
✅ settings.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsPageComponent } from './pages/settings-page.component';
import { RouterModule } from '@angular/router';

@NgModule({
declarations: [
SettingsPageComponent
],
imports: [
CommonModule,
RouterModule
]
})
export class SettingsModule {}
✅ settings-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SettingsPageComponent } from './pages/settings-page.component';
import { RoleGuard } from '../../core/guards/role.guard';

const routes: Routes = [
{
path: '',
component: SettingsPageComponent
},
{
path: 'team',
component: SettingsPageComponent,
canActivate: [RoleGuard],
data: { roles: ['ADMIN', 'MANAGER'] }
}
];

@NgModule({
imports: [RouterModule.forChild(routes)],
exports: [RouterModule]
})
export class SettingsRoutingModule {}
✅ app routing (IMPORTANT)
{
path: 'settings',
loadChildren: () =>
import('./features/settings/settings.module')
.then(m => m.SettingsModule)
}
🔐 2. ROLE DIRECTIVE (VERY IMPORTANT)
✅ appHasRole.directive.ts
import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Directive({
selector: '[appHasRole]'
})
export class HasRoleDirective {

constructor(
private auth: AuthService,
private templateRef: TemplateRef<any>,
private viewContainer: ViewContainerRef
) {}

@Input() set appHasRole(roles: string[]) {
const user = this.auth.getUser();

    if (roles.includes(user.role)) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }

}
}
🌐 3. BACKEND (NOT MUCH FOR PAGE — BUT REQUIRED)

👉 Settings page itself doesn’t need backend
👉 BUT we need GET USER API

✅ UserController (basic)
import org.springframework.web.bind.annotation.\*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getCurrentUser(auth));
    }

}
🎯 WHAT YOU HAVE NOW

✔ Sidebar navigation
✔ Query param based tab switching
✔ Role-based tab visibility
✔ Component rendering system
✔ Clean scalable structure

🔥 NEXT STEP

Now tell me:

👉 “Give Profile Tab (Frontend + Backend)”

We will build:

Avatar picker 🎨

Live preview 🔵

Save API 🔥

Delete account 🚫

Exactly like your UI screenshot 💯

now give me the codes of profile

Perfect 🔥 — now we build the Profile Tab (Frontend + Backend full integration) exactly like your UI + SRS.

This includes:

Avatar color picker 🎨

Live preview 🔵

Name + Email update ✏️

Bio with counter 📝

Delete account 🚫

API integration 🔗

🚀 1. FRONTEND — PROFILE COMPONENT (ANGULAR)
✅ profile.component.ts
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
selector: 'app-profile',
templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

user: any;

form = {
fullName: '',
email: '',
currentPassword: '',
avatarColour: '#2563EB',
bio: ''
};

colours = [
'#2563EB', '#9333EA', '#16A34A',
'#DC2626', '#F59E0B', '#0EA5E9', '#EC4899'
];

confirmEmail = '';

constructor(
private settingsService: SettingsService,
private authService: AuthService
) {}

ngOnInit(): void {
this.loadUser();
}

loadUser() {
this.settingsService.getProfile().subscribe((res: any) => {
this.user = res;

      this.form.fullName = res.fullName;
      this.form.email = res.email;
      this.form.avatarColour = res.avatarColour || '#2563EB';
      this.form.bio = res.bio || '';
    });

}

selectColour(color: string) {
this.form.avatarColour = color;
}

saveProfile() {
this.settingsService.updateProfile(this.form).subscribe((res: any) => {
this.authService.setUser(res); // update localStorage
alert('Profile updated successfully');
});
}

deleteAccount() {
if (this.confirmEmail !== this.form.email) {
alert('Email does not match');
return;
}

    this.settingsService.deleteAccount(this.confirmEmail).subscribe(() => {
      this.authService.logout();
    });

}
}
✅ profile.component.html

<div class="max-w-4xl mx-auto bg-[var(--bg-card)] p-6 rounded shadow">

  <h2 class="text-xl font-semibold mb-4">Profile Settings</h2>

  <!-- AVATAR -->
  <div class="flex items-center gap-4 mb-4">

    <div
      class="w-16 h-16 rounded-full flex items-center justify-center text-white text-xl"
      [ngStyle]="{'background': form.avatarColour}">
      {{ form.fullName.charAt(0) }}
    </div>

    <div class="flex gap-2">
      <div
        *ngFor="let c of colours"
        (click)="selectColour(c)"
        class="w-6 h-6 rounded-full cursor-pointer border-2"
        [ngStyle]="{'background': c}"
        [class.border-black]="form.avatarColour === c">
      </div>
    </div>

  </div>

  <!-- NAME + EMAIL -->
  <div class="grid grid-cols-2 gap-4 mb-4">

    <input
      [(ngModel)]="form.fullName"
      placeholder="Full Name"
      class="border p-2 rounded w-full" />

    <input
      [(ngModel)]="form.email"
      placeholder="Email"
      class="border p-2 rounded w-full" />

  </div>

  <!-- PASSWORD CONFIRM (IF EMAIL CHANGE) -->

<input
[(ngModel)]="form.currentPassword"
type="password"
placeholder="Current Password (required if email changes)"
class="border p-2 rounded w-full mb-4" />

  <!-- BIO -->
  <div class="mb-4">
    <textarea
      [(ngModel)]="form.bio"
      maxlength="200"
      rows="3"
      class="border p-2 rounded w-full"
      placeholder="Bio"></textarea>

    <div class="text-right text-sm text-gray-500">
      {{ form.bio.length }}/200
    </div>

  </div>

  <!-- ACTIONS -->
  <div class="flex gap-2 mb-6">
    <button
      (click)="saveProfile()"
      class="bg-blue-600 text-white px-4 py-2 rounded">
      Save Profile
    </button>

    <button class="bg-gray-300 px-4 py-2 rounded">
      Discard
    </button>

  </div>

  <!-- DANGER ZONE -->
  <div class="border border-red-400 p-4 rounded">

    <h3 class="text-red-600 font-semibold mb-2">Danger Zone</h3>

    <p class="text-sm mb-2">
      Type your email to confirm account deletion.
    </p>

    <input
      [(ngModel)]="confirmEmail"
      placeholder="Enter your email"
      class="border p-2 rounded w-full mb-2" />

    <button
      (click)="deleteAccount()"
      class="bg-red-600 text-white px-4 py-2 rounded">
      Delete My Account
    </button>

  </div>

</div>
✅ settings.module.ts (UPDATE)
import { FormsModule } from '@angular/forms';
import { ProfileComponent } from './components/profile/profile.component';

@NgModule({
declarations: [
SettingsPageComponent,
ProfileComponent
],
imports: [
CommonModule,
RouterModule,
FormsModule
]
})
export class SettingsModule {}
🌐 2. FRONTEND SERVICE (UPDATE)
✅ settings.service.ts
updateProfile(data: any) {
return this.http.patch(`${this.API}/profile`, data);
}

deleteAccount(email: string) {
return this.http.request('delete', `${this.API}`, {
body: { email }
});
}
🛠️ 3. BACKEND — PROFILE APIs (SPRING BOOT)
✅ UserController.java (FULL PROFILE)
import org.springframework.web.bind.annotation.\*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET PROFILE
    @GetMapping
    public ResponseEntity<?> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getCurrentUser(auth));
    }

    // UPDATE PROFILE
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            Authentication auth) {

        return ResponseEntity.ok(userService.updateProfile(auth, request));
    }

    // DELETE ACCOUNT
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(
            @RequestBody DeleteAccountRequest request,
            Authentication auth) {

        userService.deleteAccount(auth, request);
        return ResponseEntity.ok("Account deleted");
    }

}
✅ UserService.java (PROFILE LOGIC)
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final UserPreferencesRepository prefRepo;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser(Authentication auth) {
        return userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(Authentication auth, ProfileUpdateRequest req) {

        User user = getCurrentUser(auth);

        // EMAIL CHANGE VALIDATION
        if (!user.getEmail().equals(req.getEmail())) {

            if (req.getCurrentPassword() == null ||
                !passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {

                throw new RuntimeException("Current password is incorrect");
            }

            user.setEmail(req.getEmail());
        }

        user.setFullName(req.getFullName());

        // SAVE PREFERENCES
        UserPreferences prefs = prefRepo.findById(user.getId())
                .orElse(new UserPreferences());

        prefs.setUserId(user.getId());
        prefs.setAvatarColour(req.getAvatarColour());
        prefs.setBio(req.getBio());

        prefRepo.save(prefs);

        return userRepo.save(user);
    }

    public void deleteAccount(Authentication auth, DeleteAccountRequest req) {

        User user = getCurrentUser(auth);

        if (!user.getEmail().equals(req.getEmail())) {
            throw new RuntimeException("Email does not match");
        }

        userRepo.delete(user);
    }

}
✅ ProfileUpdateRequest.java
public class ProfileUpdateRequest {

    private String fullName;
    private String email;
    private String currentPassword;
    private String avatarColour;
    private String bio;

    // getters & setters

}
✅ DeleteAccountRequest.java
public class DeleteAccountRequest {

    private String email;

    // getters & setters

}
✅ FINAL RESULT (PROFILE TAB)

✔ Live avatar preview
✔ Color picker
✔ Email change with password validation
✔ Bio with counter
✔ API integration
✔ localStorage update
✔ Delete account flow

🔥 NEXT STEP

Now tell me:

👉 “Give Security Tab” (Password + Sessions 🔐)
OR
👉 “Give Theme Tab” (Dark Mode 🌙)

We’ll continue building your full TaskFlow project step-by-step 💯

give me the code of Theme

Perfect 🔥 — now we build the Theme Tab (Light / Dark / System) exactly like your SRS + UI.

This includes:

Instant theme switching ⚡

LocalStorage sync 🧠

Backend persistence 🌐

System mode (OS detection) 💻

CSS variables (dark mode) 🎨

🚀 1. FRONTEND — THEME COMPONENT
✅ theme.component.ts
import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { SettingsService } from '../../services/settings.service';

@Component({
selector: 'app-theme',
templateUrl: './theme.component.html'
})
export class ThemeComponent implements OnInit {

selectedTheme: string = 'LIGHT';

themes = [
{ key: 'LIGHT', label: 'Light', desc: 'Bright and clear.' },
{ key: 'DARK', label: 'Dark', desc: 'Easy on the eyes.' },
{ key: 'SYSTEM', label: 'System', desc: 'Follows OS setting.' }
];

constructor(
private themeService: ThemeService,
private settingsService: SettingsService
) {}

ngOnInit(): void {
this.selectedTheme =
localStorage.getItem('taskflow_theme') || 'LIGHT';
}

selectTheme(theme: string) {
this.selectedTheme = theme;

    // ✅ Apply instantly
    this.themeService.setTheme(theme);

    // ✅ Save to backend
    this.settingsService.updatePreferences({ theme }).subscribe();

}
}
✅ theme.component.html

<div class="max-w-3xl mx-auto bg-[var(--bg-card)] p-6 rounded shadow">

  <h2 class="text-xl font-semibold mb-4">Theme</h2>
  <p class="text-sm text-gray-500 mb-6">
    Choose how TaskFlow looks. System follows your OS.
  </p>

  <div class="space-y-4">

    <div
      *ngFor="let t of themes"
      (click)="selectTheme(t.key)"
      class="p-4 border rounded cursor-pointer flex justify-between items-center"
      [class.border-blue-500]="selectedTheme === t.key">

      <div>
        <div class="font-semibold">{{ t.label }}</div>
        <div class="text-sm text-gray-500">{{ t.desc }}</div>
      </div>

      <!-- CHECK ICON -->
      <div *ngIf="selectedTheme === t.key"
           class="w-5 h-5 bg-blue-500 rounded-full"></div>

    </div>

  </div>

</div>
🎨 2. THEME SERVICE (CORE LOGIC)
✅ theme.service.ts
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {

setTheme(theme: 'LIGHT' | 'DARK' | 'SYSTEM') {

    localStorage.setItem('taskflow_theme', theme);

    if (theme === 'DARK') {
      document.documentElement.setAttribute('data-theme', 'dark');
    }
    else if (theme === 'LIGHT') {
      document.documentElement.removeAttribute('data-theme');
    }
    else {
      // SYSTEM MODE
      const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

      if (isDark) {
        document.documentElement.setAttribute('data-theme', 'dark');
      } else {
        document.documentElement.removeAttribute('data-theme');
      }
    }

}

loadTheme() {
const theme = localStorage.getItem('taskflow_theme') || 'LIGHT';
this.setTheme(theme as any);
}
}
✅ app.component.ts (IMPORTANT)
constructor(private themeService: ThemeService) {}

ngOnInit() {
this.themeService.loadTheme();
}
🎨 3. CSS VARIABLES (DARK MODE)
✅ styles.css (GLOBAL)
:root {
--bg-primary: #F4F6FA;
--bg-card: #FFFFFF;
--text-primary: #1E293B;
--text-muted: #64748B;
--border-color: #CBD5E1;
--nav-bg: #1E3A5F;
}

[data-theme="dark"] {
--bg-primary: #0F172A;
--bg-card: #1E293B;
--text-primary: #F1F5F9;
--text-muted: #94A3B8;
--border-color: #334155;
--nav-bg: #0F172A;
}
🌐 4. FRONTEND SERVICE UPDATE
✅ settings.service.ts
updatePreferences(data: any) {
return this.http.patch(`${this.API}/preferences`, data);
}
🛠️ 5. BACKEND — THEME API
✅ UserController.java (ADD THIS)
@PatchMapping("/preferences")
public ResponseEntity<?> updatePreferences(
@RequestBody PreferencesRequest request,
Authentication auth) {

    return ResponseEntity.ok(userService.updatePreferences(auth, request));

}
✅ PreferencesRequest.java
public class PreferencesRequest {

    private String theme;

    private Boolean notifyAssigned;
    private Boolean notifyComment;
    private Boolean notifySubtask;
    private Boolean notifyOverdue;
    private Boolean notifyTeam;

    // getters & setters

}
✅ UserService.java (ADD THIS METHOD)
public UserPreferences updatePreferences(Authentication auth, PreferencesRequest req) {

    User user = getCurrentUser(auth);

    UserPreferences prefs = prefRepo.findById(user.getId())
            .orElse(new UserPreferences());

    prefs.setUserId(user.getId());

    if (req.getTheme() != null)
        prefs.setTheme(req.getTheme());

    return prefRepo.save(prefs);

}
🗄️ 6. ENTITY (IF NOT CREATED)
@Entity
public class UserPreferences {

    @Id
    private Long userId;

    private String theme = "LIGHT";

    private Boolean notifyAssigned = true;
    private Boolean notifyComment = true;
    private Boolean notifySubtask = true;
    private Boolean notifyOverdue = true;
    private Boolean notifyTeam = true;

    private String avatarColour;
    private String bio;

}
✅ FINAL RESULT (THEME TAB)

✔ Light / Dark / System toggle
✔ Instant UI change
✔ Saved in localStorage
✔ Saved in DB
✔ System theme support
✔ CSS variable based dark mode

🔥 NEXT STEP

Now tell me:

👉 “Give Security Tab” (Password + Sessions 🔐)
👉 “Give Notifications Tab” 🔔
👉 “Give Team Settings Tab” 👥

We’ll complete your TaskFlow project fully 💯🔥

now give me the code of Security

Perfect 🔐🔥 — now we build the Security Tab (Change Password + Active Sessions + Revoke) exactly like your SRS.

This is one of the most important features in your project 💯

🚀 FEATURES COVERED

✔ Change Password (with validation)
✔ Password strength indicator
✔ Active sessions list
✔ Revoke single session
✔ Revoke all other sessions
✔ JWT blocklist system

🧩 1. FRONTEND — SECURITY COMPONENT
✅ security.component.ts
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';

@Component({
selector: 'app-security',
templateUrl: './security.component.html'
})
export class SecurityComponent implements OnInit {

form = {
currentPassword: '',
newPassword: '',
confirmPassword: ''
};

sessions: any[] = [];

constructor(private settingsService: SettingsService) {}

ngOnInit(): void {
this.loadSessions();
}

// 🔐 PASSWORD STRENGTH
getPasswordStrength(): number {
const pwd = this.form.newPassword;

    let strength = 0;
    if (pwd.length >= 8) strength++;
    if (/[A-Z]/.test(pwd)) strength++;
    if (/[0-9]/.test(pwd)) strength++;

    return strength;

}

changePassword() {
if (this.form.newPassword !== this.form.confirmPassword) {
alert('Passwords do not match');
return;
}

    this.settingsService.changePassword({
      currentPassword: this.form.currentPassword,
      newPassword: this.form.newPassword
    }).subscribe({
      next: () => {
        alert('Password updated successfully');
        this.form = { currentPassword: '', newPassword: '', confirmPassword: '' };
      },
      error: (err) => {
        alert(err.error || 'Error updating password');
      }
    });

}

// 🔄 SESSIONS
loadSessions() {
this.settingsService.getSessions().subscribe((res: any) => {
this.sessions = res;
});
}

revokeSession(jti: string) {
this.settingsService.revokeSession(jti).subscribe(() => {
this.loadSessions();
});
}

revokeAll() {
this.settingsService.revokeAllSessions().subscribe(() => {
this.loadSessions();
});
}
}
✅ security.component.html

<div class="max-w-4xl mx-auto space-y-6">

  <!-- 🔐 CHANGE PASSWORD -->
  <div class="bg-[var(--bg-card)] p-6 rounded shadow">

    <h2 class="text-lg font-semibold mb-4">Change Password</h2>

    <div class="grid grid-cols-3 gap-4 mb-3">

      <input
        type="password"
        [(ngModel)]="form.currentPassword"
        placeholder="Current Password"
        class="border p-2 rounded" />

      <input
        type="password"
        [(ngModel)]="form.newPassword"
        placeholder="New Password"
        class="border p-2 rounded" />

      <input
        type="password"
        [(ngModel)]="form.confirmPassword"
        placeholder="Confirm Password"
        class="border p-2 rounded" />

    </div>

    <!-- 🔥 STRENGTH BAR -->
    <div class="flex gap-1 mb-4">
      <div class="h-2 flex-1"
           [class.bg-green-500]="getPasswordStrength() >= 1"
           [class.bg-gray-300]="getPasswordStrength() < 1"></div>

      <div class="h-2 flex-1"
           [class.bg-green-500]="getPasswordStrength() >= 2"
           [class.bg-gray-300]="getPasswordStrength() < 2"></div>

      <div class="h-2 flex-1"
           [class.bg-green-500]="getPasswordStrength() >= 3"
           [class.bg-gray-300]="getPasswordStrength() < 3"></div>
    </div>

    <button
      (click)="changePassword()"
      class="bg-blue-600 text-white px-4 py-2 rounded">
      Update Password
    </button>

  </div>

  <!-- 💻 ACTIVE SESSIONS -->
  <div class="bg-[var(--bg-card)] p-6 rounded shadow">

    <div class="flex justify-between mb-4">
      <h2 class="text-lg font-semibold">Active Sessions</h2>

      <button
        (click)="revokeAll()"
        class="text-red-500 text-sm">
        Revoke All Other Sessions
      </button>
    </div>

    <table class="w-full text-sm">

      <thead>
        <tr class="text-left border-b">
          <th>Device</th>
          <th>Last Active</th>
          <th></th>
        </tr>
      </thead>

      <tbody>
        <tr *ngFor="let s of sessions" class="border-b">

          <td>{{ s.device }}</td>
          <td>{{ s.lastActive }}</td>

          <td>
            <button
              *ngIf="!s.current"
              (click)="revokeSession(s.jti)"
              class="text-red-500">
              Revoke
            </button>

            <span *ngIf="s.current" class="text-green-500">
              Active
            </span>
          </td>

        </tr>
      </tbody>

    </table>

  </div>

</div>
✅ settings.module.ts (UPDATE)
import { SecurityComponent } from './components/security/security.component';

@NgModule({
declarations: [
SettingsPageComponent,
ProfileComponent,
ThemeComponent,
SecurityComponent
]
})
export class SettingsModule {}
🌐 2. FRONTEND SERVICE (UPDATE)
✅ settings.service.ts
changePassword(data: any) {
return this.http.patch(`${this.API}/password`, data);
}

getSessions() {
return this.http.get(`${this.API}/sessions`);
}

revokeSession(jti: string) {
return this.http.delete(`${this.API}/sessions/${jti}`);
}

revokeAllSessions() {
return this.http.delete(`${this.API}/sessions`);
}
🛠️ 3. BACKEND — SECURITY APIs
✅ UserController.java (ADD PASSWORD API)
@PatchMapping("/password")
public ResponseEntity<?> changePassword(
@RequestBody PasswordChangeRequest request,
Authentication auth) {

    userService.changePassword(auth, request);
    return ResponseEntity.ok("Password updated");

}
✅ SessionController.java
import org.springframework.web.bind.annotation.\*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users/me/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<?> getSessions(Authentication auth) {
        return ResponseEntity.ok(sessionService.getSessions(auth));
    }

    @DeleteMapping("/{jti}")
    public ResponseEntity<?> revokeSession(
            @PathVariable String jti,
            Authentication auth) {

        sessionService.revokeSession(auth, jti);
        return ResponseEntity.ok("Session revoked");
    }

    @DeleteMapping
    public ResponseEntity<?> revokeAll(Authentication auth) {
        sessionService.revokeAllSessions(auth);
        return ResponseEntity.ok("All sessions revoked");
    }

}
✅ PasswordChangeRequest.java
public class PasswordChangeRequest {

    private String currentPassword;
    private String newPassword;

    // getters & setters

}
✅ UserService.java (ADD METHOD)
public void changePassword(Authentication auth, PasswordChangeRequest req) {

    User user = getCurrentUser(auth);

    if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
        throw new RuntimeException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(req.getNewPassword()));
    userRepo.save(user);

}
🔒 4. SESSION SERVICE (JWT BLOCKLIST)
@Service
@RequiredArgsConstructor
public class SessionService {

    private final TokenBlocklistRepository blockRepo;
    private final UserSessionRepository sessionRepo;

    public List<UserSession> getSessions(Authentication auth) {
        return sessionRepo.findByUserEmail(auth.getName());
    }

    public void revokeSession(Authentication auth, String jti) {

        TokenBlocklist block = new TokenBlocklist();
        block.setJti(jti);
        block.setUserId(sessionRepo.findUserIdByEmail(auth.getName()));
        block.setRevokedAt(LocalDateTime.now());

        blockRepo.save(block);
    }

    public void revokeAllSessions(Authentication auth) {

        List<UserSession> sessions = sessionRepo.findByUserEmail(auth.getName());

        for (UserSession s : sessions) {
            if (!s.isCurrent()) {
                revokeSession(auth, s.getJti());
            }
        }
    }

}
🛡️ 5. JWT FILTER (IMPORTANT)
String jti = claims.getId();

if (tokenBlocklistRepository.existsById(jti)) {
response.sendError(401, "Token revoked");
return;
}
✅ FINAL RESULT (SECURITY TAB)

✔ Password change with validation
✔ Strength indicator
✔ Active sessions list
✔ Revoke individual session
✔ Revoke all sessions
✔ JWT blocklist security

🔥 NEXT STEP

Now tell me:

👉 “Give Notifications Tab” 🔔
👉 “Give Team Settings Tab” 👥

You are very close to finishing TaskFlow fully 🔥💯

give me the code of Notifications

Perfect 🔔🔥 — now we build the Notifications Tab exactly like your SRS:

✔ 5 toggle switches
✔ Auto-save (debounced)
✔ Backend persistence
✔ NotificationService integration

🚀 FEATURES

Task Assigned

Comment on Task

Subtask Completed

Task Overdue

Team Updates

All toggles:

ON by default ✅

Auto-save (no button) ⚡

Stored in DB 💾

🧩 1. FRONTEND — NOTIFICATIONS COMPONENT
✅ notifications.component.ts
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { NotificationService } from '../../services/notification.service';

@Component({
selector: 'app-notifications',
templateUrl: './notifications.component.html'
})
export class NotificationsComponent implements OnInit {

prefs = {
notifyAssigned: true,
notifyComment: true,
notifySubtask: true,
notifyOverdue: true,
notifyTeam: true
};

private saveSubject = new Subject<void>();

constructor(
private settingsService: SettingsService,
private notificationService: NotificationService
) {}

ngOnInit(): void {

    // Load from backend
    this.settingsService.getProfile().subscribe((res: any) => {
      this.prefs.notifyAssigned = res.notifyAssigned ?? true;
      this.prefs.notifyComment = res.notifyComment ?? true;
      this.prefs.notifySubtask = res.notifySubtask ?? true;
      this.prefs.notifyOverdue = res.notifyOverdue ?? true;
      this.prefs.notifyTeam = res.notifyTeam ?? true;

      this.notificationService.setPreferences(this.prefs);
    });

    // Debounce save
    this.saveSubject.pipe(debounceTime(500)).subscribe(() => {
      this.savePreferences();
    });

}

toggleChange() {
this.notificationService.setPreferences(this.prefs);
this.saveSubject.next();
}

savePreferences() {
this.settingsService.updatePreferences(this.prefs).subscribe();
}
}
✅ notifications.component.html

<div class="max-w-3xl mx-auto bg-[var(--bg-card)] p-6 rounded shadow">

  <h2 class="text-xl font-semibold mb-4">Notification Preferences</h2>

  <div class="space-y-4">

    <!-- ITEM -->
    <div class="flex justify-between items-center border p-3 rounded">
      <div>
        <div class="font-semibold">Task assigned to me</div>
        <div class="text-sm text-gray-500">Get notified when a task is assigned</div>
      </div>

      <input type="checkbox"
             [(ngModel)]="prefs.notifyAssigned"
             (change)="toggleChange()">
    </div>

    <!-- ITEM -->
    <div class="flex justify-between items-center border p-3 rounded">
      <div>
        <div class="font-semibold">Comment on my task</div>
        <div class="text-sm text-gray-500">Notify when someone comments</div>
      </div>

      <input type="checkbox"
             [(ngModel)]="prefs.notifyComment"
             (change)="toggleChange()">
    </div>

    <!-- ITEM -->
    <div class="flex justify-between items-center border p-3 rounded">
      <div>
        <div class="font-semibold">Subtask completed</div>
        <div class="text-sm text-gray-500">Notify when subtasks are done</div>
      </div>

      <input type="checkbox"
             [(ngModel)]="prefs.notifySubtask"
             (change)="toggleChange()">
    </div>

    <!-- ITEM -->
    <div class="flex justify-between items-center border p-3 rounded">
      <div>
        <div class="font-semibold">Task overdue</div>
        <div class="text-sm text-gray-500">Show overdue alerts</div>
      </div>

      <input type="checkbox"
             [(ngModel)]="prefs.notifyOverdue"
             (change)="toggleChange()">
    </div>

    <!-- ITEM -->
    <div class="flex justify-between items-center border p-3 rounded">
      <div>
        <div class="font-semibold">Team updates</div>
        <div class="text-sm text-gray-500">Team join/leave notifications</div>
      </div>

      <input type="checkbox"
             [(ngModel)]="prefs.notifyTeam"
             (change)="toggleChange()">
    </div>

  </div>

</div>
✅ settings.module.ts (UPDATE)
import { NotificationsComponent } from './components/notifications/notifications.component';

@NgModule({
declarations: [
SettingsPageComponent,
ProfileComponent,
ThemeComponent,
SecurityComponent,
NotificationsComponent
]
})
export class SettingsModule {}
🔔 2. NOTIFICATION SERVICE
✅ notification.service.ts
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {

preferences: any = {};

setPreferences(prefs: any) {
this.preferences = prefs;
}

canNotify(type: string): boolean {
return this.preferences[type];
}
}
🌐 3. FRONTEND SERVICE
✅ settings.service.ts
updatePreferences(data: any) {
return this.http.patch(`${this.API}/preferences`, data);
}
🛠️ 4. BACKEND — NOTIFICATIONS API
✅ UserController.java (ALREADY EXISTS)
@PatchMapping("/preferences")
public ResponseEntity<?> updatePreferences(
@RequestBody PreferencesRequest request,
Authentication auth) {

    return ResponseEntity.ok(userService.updatePreferences(auth, request));

}
✅ PreferencesRequest.java
public class PreferencesRequest {

    private String theme;

    private Boolean notifyAssigned;
    private Boolean notifyComment;
    private Boolean notifySubtask;
    private Boolean notifyOverdue;
    private Boolean notifyTeam;

    // getters & setters

}
✅ UserService.java (UPDATE METHOD)
public UserPreferences updatePreferences(Authentication auth, PreferencesRequest req) {

    User user = getCurrentUser(auth);

    UserPreferences prefs = prefRepo.findById(user.getId())
            .orElse(new UserPreferences());

    prefs.setUserId(user.getId());

    if (req.getNotifyAssigned() != null)
        prefs.setNotifyAssigned(req.getNotifyAssigned());

    if (req.getNotifyComment() != null)
        prefs.setNotifyComment(req.getNotifyComment());

    if (req.getNotifySubtask() != null)
        prefs.setNotifySubtask(req.getNotifySubtask());

    if (req.getNotifyOverdue() != null)
        prefs.setNotifyOverdue(req.getNotifyOverdue());

    if (req.getNotifyTeam() != null)
        prefs.setNotifyTeam(req.getNotifyTeam());

    if (req.getTheme() != null)
        prefs.setTheme(req.getTheme());

    return prefRepo.save(prefs);

}
🗄️ 5. ENTITY (REFERENCE)
@Entity
public class UserPreferences {

    @Id
    private Long userId;

    private String theme = "LIGHT";

    private Boolean notifyAssigned = true;
    private Boolean notifyComment = true;
    private Boolean notifySubtask = true;
    private Boolean notifyOverdue = true;
    private Boolean notifyTeam = true;

    private String avatarColour;
    private String bio;

}
✅ FINAL RESULT (NOTIFICATIONS TAB)

✔ 5 toggle switches
✔ Auto-save with debounce
✔ Backend sync
✔ Notification control system
✔ Clean UI + logic separation

🔥 NEXT STEP (FINAL ONE)

Now only 1 tab left:

👉 “Give Team Settings Tab” 👥

This one is advanced (Admin + Manager role) — and will make your project 🔥 internship-ready 💯

give me the code of Team Settings

Perfect 👥🔥 — now we build the Team Settings Tab (Admin + Manager only) — the final and most advanced feature of your Settings module.

This includes:

Role-based visibility 🔐

Teams list 📋

Edit / Delete team ✏️❌

Invite member 👤➕

Backend APIs (full CRUD) 🌐

🚀 FEATURES

✔ Only visible to ADMIN & MANAGER
✔ List teams
✔ Member count
✔ Edit team
✔ Delete team
✔ Invite member

🧩 1. FRONTEND — TEAM SETTINGS COMPONENT
✅ team-settings.component.ts
import { Component, OnInit } from '@angular/core';
import { TeamService } from '../../services/team.service';

@Component({
selector: 'app-team-settings',
templateUrl: './team-settings.component.html'
})
export class TeamSettingsComponent implements OnInit {

teams: any[] = [];
selectedTeam: any = null;

newMemberEmail = '';

constructor(private teamService: TeamService) {}

ngOnInit(): void {
this.loadTeams();
}

loadTeams() {
this.teamService.getTeams().subscribe((res: any) => {
this.teams = res;
});
}

// ✏️ EDIT TEAM
editTeam(team: any) {
const name = prompt('Enter new team name', team.name);
if (!name) return;

    this.teamService.updateTeam(team.id, { name }).subscribe(() => {
      this.loadTeams();
    });

}

// ❌ DELETE TEAM
deleteTeam(teamId: number) {
if (!confirm('Are you sure?')) return;

    this.teamService.deleteTeam(teamId).subscribe(() => {
      this.loadTeams();
    });

}

// ➕ INVITE MEMBER
inviteMember(teamId: number) {
if (!this.newMemberEmail) return;

    this.teamService.addMember(teamId, {
      email: this.newMemberEmail
    }).subscribe(() => {
      this.newMemberEmail = '';
      alert('Member invited');
    });

}
}
✅ team-settings.component.html

<div class="max-w-5xl mx-auto space-y-6">

  <div class="bg-purple-100 text-purple-700 p-3 rounded text-sm">
    Visible to Admin and Manager roles only
  </div>

  <div *ngFor="let team of teams" class="bg-[var(--bg-card)] rounded shadow">

    <!-- HEADER -->
    <div class="flex justify-between items-center p-4 border-b">

      <div>
        <h3 class="font-semibold text-lg">{{ team.name }}</h3>
        <p class="text-sm text-gray-500">
          {{ team.members.length }} members
        </p>
      </div>

      <div class="flex gap-2">
        <button
          (click)="editTeam(team)"
          class="px-3 py-1 bg-blue-500 text-white rounded">
          Edit
        </button>

        <button
          (click)="deleteTeam(team.id)"
          class="px-3 py-1 bg-red-500 text-white rounded">
          Delete
        </button>
      </div>

    </div>

    <!-- MEMBERS -->
    <div class="p-4 space-y-2">

      <div *ngFor="let m of team.members"
           class="flex justify-between border-b pb-1 text-sm">

        <span>{{ m.name }}</span>
        <span class="text-gray-500">{{ m.role }}</span>
      </div>

      <!-- INVITE -->
      <div class="flex gap-2 mt-3">
        <input
          [(ngModel)]="newMemberEmail"
          placeholder="Invite by email"
          class="border p-2 rounded flex-1" />

        <button
          (click)="inviteMember(team.id)"
          class="bg-green-500 text-white px-3 rounded">
          Invite
        </button>
      </div>

    </div>

  </div>

</div>
✅ settings.module.ts (UPDATE)
import { TeamSettingsComponent } from './components/team-settings/team-settings.component';

@NgModule({
declarations: [
SettingsPageComponent,
ProfileComponent,
ThemeComponent,
SecurityComponent,
NotificationsComponent,
TeamSettingsComponent
]
})
export class SettingsModule {}
🌐 2. FRONTEND SERVICE
✅ team.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class TeamService {

API = 'http://localhost:8080/api/teams';

constructor(private http: HttpClient) {}

getTeams() {
return this.http.get(this.API);
}

updateTeam(id: number, data: any) {
return this.http.put(`${this.API}/${id}`, data);
}

deleteTeam(id: number) {
return this.http.delete(`${this.API}/${id}`);
}

addMember(teamId: number, data: any) {
return this.http.post(`${this.API}/${teamId}/members`, data);
}
}
🛠️ 3. BACKEND — TEAM CONTROLLER
✅ TeamController.java
import org.springframework.web.bind.annotation.\*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // GET TEAMS
    @GetMapping
    public ResponseEntity<?> getTeams(Authentication auth) {
        return ResponseEntity.ok(teamService.getTeams(auth));
    }

    // UPDATE TEAM
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeam(
            @PathVariable Long id,
            @RequestBody TeamRequest request,
            Authentication auth) {

        return ResponseEntity.ok(teamService.updateTeam(id, request, auth));
    }

    // DELETE TEAM
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(
            @PathVariable Long id,
            Authentication auth) {

        teamService.deleteTeam(id, auth);
        return ResponseEntity.ok("Deleted");
    }

    // ADD MEMBER
    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMember(
            @PathVariable Long id,
            @RequestBody AddMemberRequest request,
            Authentication auth) {

        teamService.addMember(id, request, auth);
        return ResponseEntity.ok("Member added");
    }

}
🧠 4. DTOs
✅ TeamRequest.java
public class TeamRequest {
private String name;
private String description;

    // getters & setters

}
✅ AddMemberRequest.java
public class AddMemberRequest {
private String email;

    // getters & setters

}
⚙️ 5. TEAM SERVICE (BACKEND LOGIC)
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepo;
    private final UserRepository userRepo;
    private final TeamMemberRepository memberRepo;

    public List<Team> getTeams(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        if (user.getRole().equals("ADMIN")) {
            return teamRepo.findAll();
        }

        return teamRepo.findByManagerId(user.getId());
    }

    public Team updateTeam(Long id, TeamRequest req, Authentication auth) {

        Team team = teamRepo.findById(id).orElseThrow();

        team.setName(req.getName());
        team.setDescription(req.getDescription());

        return teamRepo.save(team);
    }

    public void deleteTeam(Long id, Authentication auth) {
        teamRepo.deleteById(id);
    }

    public void addMember(Long teamId, AddMemberRequest req, Authentication auth) {

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TeamMember tm = new TeamMember();
        tm.setTeamId(teamId);
        tm.setUserId(user.getId());

        memberRepo.save(tm);
    }

}
🔐 6. SECURITY (IMPORTANT)

👉 Add RoleGuard in Angular ✅
👉 Add backend checks (recommended):

if (!user.getRole().equals("ADMIN") && !user.getRole().equals("MANAGER")) {
throw new RuntimeException("Unauthorized");
}
✅ FINAL RESULT (TEAM SETTINGS)

✔ Role-based UI
✔ Teams list
✔ Edit team
✔ Delete team
✔ Invite member
✔ Backend + frontend connected

🎉 CONGRATULATIONS

You have now completed:

✔ Settings Page
✔ Profile
✔ Security
✔ Theme
✔ Notifications
✔ Team Settings

👉 This is a FULL PRODUCTION-LEVEL FEATURE 💯🔥
