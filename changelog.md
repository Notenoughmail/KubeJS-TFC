### 0.6.1
- Add `tfc.data` and `tfc.worldgen.data` events to replace a mixin into `DataPackEventJS`
- Deprecate the data builder methods currently present in the `server.datapack.*` events, they are now available in the new events above

### 0.6.0
⚠️ This update has major breaking changes, see below ⚠️
- Add `tfc.collapse` server event
- Events fired on the server (`tfc.climate_model.select`, `tfc.start_fire`, `tfc.prospect`, `tfc.logging`, and `tfc.animal_product`) are now able to be handled in server scripts
- The `tfc.prospect` event now has a method to get its `ProspectResult`
- Add `tfc:tool` item type
  - Has `.hammer()` and `.knife()` methods
- Minimum TFC version is now `2.2.29`
- The `tfc.logging` event now provides a `LevelJS` and `BlockContainerJS`, instead of a raw `LevelAccessor`, `BlockState`, and `BlockPos`
- Data builders in the `server.datapack.*` events now have an optional `ResourceLocation` as a final argument
  - Unfortunately, this means that several once optional arguments are no longer optional
    - For item heat definitions: The forging and welding temps are no longer optional, but nullable
    - For metal definitions: The tier is no longer optional
	- For panning definitions: The model stages are no longer a `String...`, but a `List<String>`, just wrap your model names in square brackets to update
- Add data builders for climate ranges and fauna
- Worldgen builders in `server.datapack.*` events can now specify the namespace of the name (allows overwriting default features), if no colon is found, the namespace will default to `kubejs_tfc` as it did before
- Add the ability to access TFC player data (nutrition, chisel mode, intoxication) through `PlayerJS#getData()`
- Modification and removal of default rock layers now fires as late as possible

### 0.5.5
- Fix a problem introduced in 0.5.3 that caused any delegate recipe types with ItemStackProvider outputs to fail
- Add AFC tree tapping recipe type

### 0.5.4
- Add the ability to register custom climate models

### 0.5.3
- Fix delegate recipe types failing to capture in/outputs of json recipes
- Add js events for climate model selection, fire start, prospecting, logging, and animal product events
- Rename `rock_settings.register` to `tfc.rock_settings.register`
- Remove deprecated things

### 0.5.2
- Add raw rock builder
- Add meal item stack provider modifier

### 0.5.1
- Fix a problem where slashing damage resistance couldn't be added

### 0.5.0
- Add recipe filters and replacement methods for TFC's non-vanilla ingredient/output types
- Fix a few bugs with container limiting slot ranges

### 0.4.4
- Add container limiting functionality based off of OversizedItemInStorageArea
- Remove @HideFromJS annotations from many methods

### 0.4.3
- Add class filters for internal Java classes
- Add block builders for moss growing/spreading blocks/stairs/slabs/walls
- Fully remove old food and drinkable data builders
- Allow fluid stack and block ingredient to take regular expression inputs
- Deprecate `addFeaturesToTFCWorld` in favor of tag event
- Add fluid container item builder

### 0.4.2
- Add the ability to add, remove, and modify default rock layers
- Add TFC ingredient constructors
- Change custom item/block builder type names to start with `tfc:` rather than `tfc_`, the old ones are still there but will be removed at some point
- Add the ability to make fluids have some spring water properties

### 0.4.1
- Add [TFC Casting With Channels](https://www.curseforge.com/minecraft/mc-mods/tfc-casting-with-channels) integration
- Conditional and set food data item stack provider modifiers
- Allow custom molds to go into mold tables
- Add FirmaLife plantable data builder
- Casting recipes now output an item stack provider to match recent TFC updates
- Fix/update vat recipes

### 0.4.0
- Add builders for several TFC world gen features
    - Veins
    - Boulders
    - Geodes
    - Thin spikes
    - If-then
- Adds cullfaces to default groundcover models
- Add constructors for sluicing and panning data
- Fix some problems with item stack modifiers sometimes not registering modifiers
- Move food and item data constructors to a better system

### 0.3.2
- Add recipe types for [TFC Rosia](https://legacy.curseforge.com/minecraft/mc-mods/tfc-rosia)

### 0.3.1
- Fix a few miss-spellings which caused certain actions to silently fail

### 0.3.0
- Add data events
  - TFC
    - damage resistances
    - drinkables
    - fertilizers
    - food items
    - fuels
    - heats
    - item sizes
    - lamp fuels
    - metals
    - supports
  - FirmaLife
    - greenhouses
  - Beneath
    - nether fertilizers

### 0.2.1
- Add loose rock, groundcover, rock spike, and thin spike block builders
- Fix some problems with TFC's crafting recipes

### 0.2.0
- Add FirmaLife recipes and item stack modifiers
- Fix all instances of ItemProvider.empty() sharing modifiers
- Allow TFC tool constructors to modify attributes
- Add TFC's crafting recipes
- Fix formatting of recipes in the log
- Fix an issue with sealed barrel recipes with item ingredients of stack size 1

### 0.1.1
- Fix/simplify some recipes

### 0.1.0
Initial Release