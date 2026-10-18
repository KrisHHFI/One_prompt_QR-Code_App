# QR Scanner

A minimal Android QR code scanner. It opens straight to the camera, then shows the result with **Copy** and **Open** buttons (Open appears for links, emails, phone numbers and map locations). Works offline, no ads, no tracking, and needs only the camera permission.

## Get the APK

1. Create a new repository on GitHub (Private is fine).
2. Upload everything in this folder to it. In the browser: **Add file → Upload files**, drag in the contents of this folder (including the hidden `.github` folder), then **Commit**.
   - If `.github` doesn't upload by drag-and-drop, create the file manually: **Add file → Create new file**, name it `.github/workflows/build.yml`, and paste in the contents.
3. Open the **Actions** tab. A "Build APK" run starts automatically and takes about 3–5 minutes.
4. When it finishes with a green tick, open **Releases** (right-hand side of the repo page) and download `QRScanner.apk`. Do this on your phone, signed in to GitHub.

## Install on your phone

1. Tap the downloaded `QRScanner.apk`.
2. Android will ask to allow installs from your browser or file manager. Allow it for this install.
3. Play Protect may warn that the app is unknown. Choose **Install anyway**, since it's your own build.
4. Open **QR Scanner** and allow camera access the first time.

## Rebuilding

Any change you commit triggers a new build and a new release.
