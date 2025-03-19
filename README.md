# <img src="https://github.com/robertlevonyan/media-picker/blob/master/Images/logo.png"  width="25" height="25" /> &nbsp;&nbsp;&nbsp; Universal Media Picker

|Easy customizable picker for all your needs in Android application|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker.png"  width="400" />|
|----------------------------------------------------------------------------------------------|-----------|

|PickerDialogVersion|PickerComposeVersion|
|--------------------|--------------------|
|![Maven Central Version](https://img.shields.io/maven-central/v/com.robertlevonyan.components/picker)|![Maven Central Version](https://img.shields.io/maven-central/v/com.robertlevonyan.components/picker)|

## Setup

Add following line of code to your project level gradle file

```kotlin
  repositories {
    mavenCentral()
  }
```

#### Gradle:

Add following line of code to your module(app) level gradle file

```groovy
    implementation 'com.robertlevonyan.components:Picker:<PickerDialogVersion>'
```

#### Kotlin:

```kotlin
    implementation("com.robertlevonyan.components:Picker:$PickerDialogVersion")
```

#### Maven:

```xml
  <dependency>
    <groupId>com.robertlevonyan.components</groupId>
    <artifactId>Picker</artifactId>
    <version>PickerDialogVersion</version>
    <type>pom</type>
  </dependency>
```

### For Jetpack Compose version

```kotlin
    implementation("com.robertlevonyan.compose:picker:$PickerComposeVersion")
```

## Usage

Buildung picker
```kotlin
  // in fragment or activity
  pickerDialog {
    setTitle(...)          // String value or resource ID
    setTitleTextSize(...)  // Text size of title
    setTitleTextColor(...) // Color of title text
    setListType(...)       // Type of the picker, must be PickerDialog.TYPE_LIST or PickerDialog.TYPE_Grid
    setItems(...)          // List of ItemModel-s which should be in picker
 }.setPickerCloseListener { type: ItemType, uris: List<Uri> ->
   // Getting the result
   when (type) {
            ItemType.Camera -> /* do something with the photo you've taken */
            ItemType.Video -> /* do something with the video you've recorded */
            is ItemType.ImageGallery -> /* do something with the images you've chosen */
            is ItemType.AudioGallery -> /* do something with the audios you've chosen */
            is ItemType.VideoGallery -> /* do something with the videos you've chosen */
            is ItemType.Files -> /* do something with the files you've chosen */
        }
 }.show()
```

Creating items
```kotlin
  /*  Create a camera item, can be
      ItemType.Camera
      ItemType.Video
      ItemType.ImageGallery
      ItemType.AudioGallery
      ItemType.VideoGallery
      ItemType.Files
  */
  val itemModel = ItemModel(ItemModel.Camera)
  
  // Some optional parameters
  val itemModel = ItemModel(
                    ItemModel.Camera,
                    itemLabel, // Default value is "", in this case default text value will be set
                    itemIcon,  // Default value is 0, in this case default icon will be set
                    hasBackground, // draw a shape background over the icon, default value is true
                    backgroundType, // choose a type for icon background, only works if hasBackground is true, can have one of
                                    // this values: ItemType.TYPE_CIRCLE, ItemType.TYPE_SQUARE, ItemType.TYPE_ROUNDED_SQUARE
                    itemBackgroundColor, // custom color for background shape, only works if hasBackground is true, 
                                         // default color is accent color of your app
                    )
```

Now you can set custom file types for pickers
```kotlin
 MimeType.Audio.All
 MimeType.Audio.Mp3
 MimeType.Audio.M4a
 MimeType.Audio.Wav
 MimeType.Audio.Amr
 MimeType.Audio.Awb
 MimeType.Audio.Ogg
 MimeType.Audio.Aac
 MimeType.Audio.Mka
 MimeType.Audio.Midi
 MimeType.Audio.Flac

 MimeType.Video.All
 MimeType.Video.Mpeg
 MimeType.Video.Mp4
 MimeType.Video.`3gp
 MimeType.Video.Mkv
 MimeType.Video.Webm
 MimeType.Video.Avi

 MimeType.Image.All
 MimeType.Image.Jpeg
 MimeType.Image.Png
 MimeType.Image.Gif
 MimeType.Image.Bmp
 MimeType.Image.Webp

 MimeType.Files.All
 MimeType.Files.Txt
 MimeType.Files.Html
 MimeType.Files.Pdf
 MimeType.Files.Doc
 MimeType.Files.Xls
 MimeType.Files.Ppt
 MimeType.Files.Zip

 MimeType.Custom() // if you can't find your desired file type you can use this one
```

### For Jetpack Compose version

```kotlin
setContent {
 val coroutine = rememberCoroutineScope()

 PickerDialog(
   dialogTitle = stringResource(id = R.string.app_name),
   dialogTitleSize = 22.sp,
   dialogListType = ListType.TYPE_GRID,
   dialogGridSpan = 3,
   dialogItems = setOf(
     ItemModel(ItemType.Camera, backgroundType = ShapeType.TYPE_ROUNDED_SQUARE, itemBackgroundColor = Color.Red),
     ItemModel(ItemType.Video),
     ItemModel(ItemType.ImageGallery()),
     ItemModel(ItemType.AudioGallery()),
     ItemModel(ItemType.VideoGallery()),
     ItemModel(ItemType.Files())
   ),
   onItemSelected = { selectedUris ->
    // get uris
   }
 ) { bottomSheetState ->
   Column {
     Button(onClick = {
       coroutine.launch {
         if (bottomSheetState.isVisible) {
           bottomSheetState.hide()
         } else {
           bottomSheetState.show()
         }
       }
     }) {
       Text(text = "Open picker")
     }
   }
 }
}
```

And the result is

|List picker                                            |Grid picker                                                |
|-------------------------------------------------------|-----------------------------------------------------------|
|<img src="https://github.com/robertlevonyan/media-picker/blob/master/Images/picker_list.jpg"  width="500" />|<img src="https://github.com/robertlevonyan/media-picker/blob/master/Images/picker_grid.jpg"  width="500" />|

## Versions

### 2.2.0

More customizable item types

### 2.1.2 - 2.1.6

Update to Java 11
SDK 31 ready
Minor updates

#### 2.1.1

Single picker issue fixed

### 2.1.0

Migration to mavenCentral

#### 2.0.1 - 2.0.2

Refactoring and some UI changes

### 2.0.0

New version of library.
Mostly rewritten. Now supports multiple selection.

#### 1.0.1 - 1.1.5

Refactoring and some UI changes

### 1.0.0

First version of library

## Compose versions

### 1.1.0

More customizable item types

### 1.0.0

First version of library

## Contact

- **Email**: me@robertlevonyan.com
- **Website**: https://robertlevonyan.com/
- **Medium**: https://medium.com/@RobertLevonyan
- **Twitter**: https://twitter.com/@RobertLevonyan
- **Facebook**: https://facebook.com/robert.levonyan
- **Google Play**: https://play.google.com/store/apps/dev?id=5477562049350283357

Special thanks to @chimzycash for createing an amazing logo for this project ☺️

<a href="https://www.buymeacoffee.com/robertlevonyan">
  <img src="https://github.com/robertlevonyan/media-picker/blob/master/Images/coffee.jpeg"  width="300" />
</a>


## Licence

```
    Universal Media Picker©
    Copyright 2021 Robert Levonyan
    Url: https://github.com/robertlevonyan/MediaPicker

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```

<img src="https://github.com/robertlevonyan/media-picker/blob/master/Images/logo_title.png"/>
