# Universal Media Picker

|Easy customizable picker for all your needs in Android application|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker.png"  width="400" />|
|----------------------------------------------------------------------------------------------|-----------|

## Setup

#### Gradle:

Add following line of code to your module(app) level gradle file

```groovy
    implementation 'com.robertlevonyan.components:Picker:1.0.5'
```

#### Maven:

```xml
  <dependency>
    <groupId>com.robertlevonyan.components</groupId>
    <artifactId>Picker</artifactId>
    <version>1.0.5</version>
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

What it can be look like
|List picker|Grid picker|
|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker_list.jpg"  width="500" />|<img src="https://github.com/robertlevonyan/MediaPicker/blob/master/Images/picker_grid.jpg"  width="500" />|
|----------------------------------------------------------------------------------------------|-----------|
