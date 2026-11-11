; Inno Setup script for the ArabicBASIC Windows installer.
;
; Compile (from the repository root):
;   iscc /DAppVersion=0.7.3 /DSourceExe=target\arabicbasic.exe packaging\windows\arabicbasic.iss
; Output: target\installer\ArabicBASIC-<version>-windows-x64-setup.exe
;
; Installs arabicbasic.exe and (optionally, on by default) adds its folder to PATH so
; `arabicbasic` works in cmd / PowerShell. Per-user by default (no admin prompt); the setup
; dialog lets the user choose an all-users install instead.

#ifndef AppVersion
  #define AppVersion "0.0.0"
#endif
#ifndef SourceExe
  #define SourceExe "..\..\target\arabicbasic.exe"
#endif

[Setup]
AppId={{900E64E2-8AB3-46E1-83A3-308386653E7E}
AppName=ArabicBASIC
AppVersion={#AppVersion}
AppVerName=ArabicBASIC {#AppVersion}
AppPublisher=Lisantra
AppPublisherURL=https://github.com/mgatto/ArabicBASIC
AppSupportURL=https://github.com/mgatto/ArabicBASIC/issues
DefaultDirName={autopf}\ArabicBASIC
DisableProgramGroupPage=yes
PrivilegesRequired=lowest
PrivilegesRequiredOverridesAllowed=dialog
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
OutputDir=..\..\target\installer
OutputBaseFilename=ArabicBASIC-{#AppVersion}-windows-x64-setup
Compression=lzma2
SolidCompression=yes
WizardStyle=modern
ChangesEnvironment=yes
UninstallDisplayName=ArabicBASIC

[Files]
Source: "{#SourceExe}"; DestDir: "{app}"; DestName: "arabicbasic.exe"; Flags: ignoreversion

[Tasks]
Name: "addtopath"; Description: "Add ArabicBASIC to the PATH (so you can run ""arabicbasic"" from any terminal)"; GroupDescription: "Command line:"

[Registry]
; Per-user install -> HKCU PATH; all-users install -> HKLM PATH.
Root: HKCU; Subkey: "Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};{app}"; Tasks: addtopath; Check: (not IsAdminInstallMode) and NeedsAddPath(ExpandConstant('{app}'))
Root: HKLM; Subkey: "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};{app}"; Tasks: addtopath; Check: IsAdminInstallMode and NeedsAddPath(ExpandConstant('{app}'))

[Code]
function PathRootKey(): Integer;
begin
  if IsAdminInstallMode then
    Result := HKEY_LOCAL_MACHINE
  else
    Result := HKEY_CURRENT_USER;
end;

function PathSubKey(): String;
begin
  if IsAdminInstallMode then
    Result := 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
  else
    Result := 'Environment';
end;

// True when Dir is not already one of the PATH entries (avoids duplicates on reinstall).
function NeedsAddPath(Dir: String): Boolean;
var
  OrigPath: String;
begin
  if not RegQueryStringValue(PathRootKey(), PathSubKey(), 'Path', OrigPath) then
  begin
    Result := True;
    exit;
  end;
  Result := Pos(';' + Uppercase(Dir) + ';', ';' + Uppercase(OrigPath) + ';') = 0;
end;

// Removes {app} from PATH on uninstall.
procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
var
  OrigPath, Dir, Padded: String;
  P: Integer;
begin
  if CurUninstallStep <> usPostUninstall then
    exit;
  if not RegQueryStringValue(PathRootKey(), PathSubKey(), 'Path', OrigPath) then
    exit;
  Dir := ExpandConstant('{app}');
  Padded := ';' + OrigPath + ';';
  P := Pos(';' + Uppercase(Dir) + ';', Uppercase(Padded));
  if P = 0 then
    exit;
  Delete(Padded, P, Length(Dir) + 1);
  // Strip the artificial leading/trailing separators added above.
  if Copy(Padded, 1, 1) = ';' then
    Delete(Padded, 1, 1);
  if Copy(Padded, Length(Padded), 1) = ';' then
    Delete(Padded, Length(Padded), 1);
  RegWriteExpandStringValue(PathRootKey(), PathSubKey(), 'Path', Padded);
end;
