# Photography
Useful utils for managing photos on Android

# Functionality
1. Connect to desktop (Wifi) to fetch photographs.
2. Resize and sign as configured
3. Extract Metadata if available.
4. Note details including title, description, location, category, album.
5. Save photographs in a searchable JSON database (https://github.com/dizitart/nitrite-database)
6. Slideshow based on filter
7. Publish on Facebook / Instagram / Flickr - Keep track.
8. Minor tweaks to the image

# Milestones
## UI
* Display Image from external file
* Display Slides / tiles / lists
* Show / Get Image information on image click
* Organize by albums

## Connect:
* Receive image files from other apps on the phone
* Create Wifi Host
* Host a page on the mobile that can absorb image files - Check out https://github.com/solegaonkar/nanohttpd or https://developer.android.com/reference/java/net/ServerSocket.html
* HTML to drop image files one or many at a time, along with their detials.

## Camera
* Click images using the camera and absorb the file

## Image Processing:
* Read Metadata
* Library for minor tweaks to the image
* Resize image
* Add signature
* Get this running in android

## Database
* Use the metadata and user details to build a JSON
* Save image file on external memory. Change name or do something to hide it from gallery
* Push the JSON into database (https://github.com/dizitart/nitrite-database)
* API to get image sequence based on a query


# Schedule
## Learning
* Kotlin - 16 Hours

## UI
* Display Image from external file
* Display Slides / tiles / lists
* Show / Get Image information on image click
* Organize by albums
```
AlbumActivity
    - SlideActivity
    - TilesActivity
    - ListActivity

EditImageDetailsActivity

Filter??
```
