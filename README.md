# <img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/logo.png"  width="30" height="30" /> Universal Media Picker

|Easy customizable picker for all your needs in Android application|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker.png"  width="400" />|
|----------------------------------------------------------------------------------------------|-----------|

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Universal%20Media%20Picker-yellow.svg?style=flat-square)](https://android-arsenal.com/details/1/6862)[![API](https://img.shields.io/badge/API-14%2B-yellow.svg?style=flat-square)](https://android-arsenal.com/api?level=14)[ ![Download](https://api.bintray.com/packages/robertlevonyan/maven/Picker/images/download.svg) ](https://bintray.com/robertlevonyan/maven/Picker/_latestVersion)

## Setup

#### Gradle:

Add following line of code to your module(app) level gradle file

```groovy
    implementation 'com.robertlevonyan.components:Picker:1.1.1'
```

#### Maven:

```xml
  <dependency>
    <groupId>com.robertlevonyan.components</groupId>
    <artifactId>Picker</artifactId>
    <version>1.1.1</version>
    <type>pom</type>
  </dependency>
```

## Usage

Buildung picker
```kotlin
  val pickerDialog = PickerDialog.Builder(this)// Activity or Fragment
                       .setTitle(...)          // String value or resource ID
                       .setTitleTextSize(...)  // Text size of title
                       .setTitleTextColor(...) // Color of title text
                       .setListType(...)       // Type of the picker, must be PickerDialog.TYPE_LIST or PickerDialog.TYPE_Grid
                       .setItems(...)          // List of ItemModel-s which should be in picker
                       .create()               // Create picker
```
Creating items
```kotlin
  /*  Create a camera item, can be
      ItemModel.ITEM_CAMERA 
      ItemModel.ITEM_GALLERY
      ItemModel.ITEM_VIDEO
      ItemModel.ITEM_VIDEO_GALLERY 
      ItemModel.ITEM_FILES 
  */
  val itemModel = ItemModel(ItemModel.ITEM_CAMERA)
  
  // Some optional parameters
  val itemModel = ItemModel(
                    ItemModel.ITEM_CAMERA,
                    itemLabel, // Default value is "", in this case default text value will be set
                    itemIcon,  // Default value is 0, in this case default icon will be set
                    hasBackground, // draw a shape background over the icon, default value is true
                    backgroundType, // choose a type for icon background, only works if hasBackground is true, can have one of
                                    // this values: ItemType.TYPE_CIRCLE, ItemType.TYPE_SQUARE, ItemType.TYPE_ROUNDED_SQUARE
                    itemBackgroundColor, // custom color for background shape, only works if hasBackground is true, 
                                         // default color is accent color of your app
                    )
```

Passing permissions from Activity
```kotlin
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        pickerDialog.onPermissionsResult(requestCode, permissions, grantResults)
    }
    
    // or if you have a fragment
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        myFragment.pickerDialog.onPermissionsResult(requestCode, permissions, grantResults)
    }
```

Showing picker
```kotlin
  pickerDialog.show()
```

And the result is

|List picker                                            |Grid picker                                                |
|-------------------------------------------------------|-----------------------------------------------------------|
|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker_list.jpg"  width="500" />|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker_grid.jpg"  width="500" />|

Getting the result
```kotlin
    pickerDialog.setPickerCloseListener { type, uri ->
        when (type) {
            ItemModel.ITEM_CAMERA -> /* do something with the photo you've taken */
            ItemModel.ITEM_GALLERY -> /* do something with the image you've chosen */
            ItemModel.ITEM_VIDEO -> /* do something with the video you've recorded */
            ItemModel.ITEM_VIDEO_GALLERY -> /* do something with the video you've chosen */
            ItemModel.ITEM_FILES -> /* do something with the file you've chosen */
        }
     }
```
## Versions

### 1.0.0 - 1.0.5

First version of library

## Contact

- **Email**: me@robertlevonyan.com
- **Website**: https://robertlevonyan.com/
- **Medium**: https://medium.com/@RobertLevonyan
- **Twitter**: https://twitter.com/@RobertLevonyan
- **Facebook**: https://facebook.com/robert.levonyan
- **Google Play**: https://play.google.com/store/apps/dev?id=5477562049350283357

## Licence

```
    Universal Media Picker©
    Copyright 2018 Robert Levonyan
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
