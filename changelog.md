### 1.3.3 (W.I.P.)
- The list ids command is now paginated
- Add several bindings for noises
- Add some additional helper functions for TFC's `Noise2D` and `Noise3D` classes

### 1.3.2
- Add `#hasPostProcess` and `#emissiveRendering` methods to extended properties
- Add fissure, forest, forest entry, overlay tree, random tree, stacked tree, and krummholz worldgen builders
- Add direct wrapper for TFC's `ItemStackProvider`
- Fix double crops not having yield multipliers in their loot tables
- Add convenience method for setting the dead model of torches
- Add TFC sapling & leaf block builders
- Rename several methods in crop block builders to prevent Rhino throwing a fit when used
  - `#productItem(ResourceLocation)` -> `#existingProductItem(ResourceLocation)`
  - `#model(number,Consumer)` -> `#setModel(number,Consumer)`
  - `#model(Consumer)` -> `#setModel(Consumer)`
  - `#texture(number,String)` -> `#textureAt(number,String)`
  - `#texture(number,String,String)` -> `#textureAt(number,String,String)`
- Allow `RecipeJS#getOriginalRecipeIngredients()` to work with TFC recipe types
- Add ability to change custom crop growth and expiry rates
- Add `always` and `never` shortcuts for extended property settings that take `StatePredicate`s
- Add tree solver command for modpack makers to easily make tree structures with proper states
- Add event for registering ISM -> json converters. All TFC, Firmalife, and TFCCC ISM types should be handled by default
- Add the ability to retrieve `NormalNoise`s during the chunk data provider creation event
- Adds sealable inventory attachment which applies a food trait to items within it
- "Fix" proto chunks with no `ChunkData` causing issues when promoted to level chunks with custom chunk data providers
- Add custom surface rule source that uses the `RockSettings` at a given position
- Fix custom spreading and double crops not respecting custom textures set in scripts
  - This involved some reworks for all crop resource gens, the only noticeable effect should all stages looking for the *same* texture instead of unique ones, by default

### 1.3.1
- Fix supports having broken connecting models by default
- Fix mammals being unable to define their configs
- Add some flexibility to custom waterwheel texture paths
- Add validation to spike and loose blocks of custom defined `RockSettings`
- Fix noise settings of wrapped chunk generators not being used in production environments

### 1.3.0
- Add commands to print TFC's level settings and chunk data
- Add setting (default enabled) to deduplicate TFC datapack validation errors that are inserted into the KubeJS console
- Add chunk generator type `kubejs_tfc:wrapped` which enables adding chunk data to levels without proper TFC-like level generators
- New event for creating chunk data, depends on above chunk generator type
- De-hardcode dead crop model generation
- Add `tfc:torch` block builder type
- Add `tfc:heat` block entity attachment type
- Add `tfc:calendar` block entity attachment type
- Deprecated:
  - `#drips` in `tfc:thin_spike` block type, same functionality handled by nullable parameter of `#dripParticle`
  - `#hasBubbles` & `#hasSteam` in `tfc:spring` fluid type, same functionality handled by nullable parameter of `#bubbleParticle` & `#steamParticle`
  - `#allModels` in berry bushes, superseded by `#models`
- Remove deprecated methods in custom climate model event
- Remove old, non-functional chunk-data creation mechanism for custom climate models that was added in 1.2.6
- Add support for Kube's ingredient and result modifiers to TFC's advanced crafting recipes
- Fix drinkable description command printing the first effect in place of all-but-the-last effect
- Fix being unable to modify the cane block of spreading bushes
- Add the ability to properly modify the model generation of multi-model blocks (aqueducts, grass, gearboxes, etc.)

### 1.2.6
- Add TFC Log block builder
- Enable custom climate models to set chunk data for levels without TFC-like generation
- Fix `#simpleBlockState` in the worldgen data event incorrectly serializing propertyless block states 

### 1.2.5
- Fix faulty assumption that climate models will only be added to TFC-like worlds
- Slight rework to how custom climate models are created
  - 'Advanced' climate model functionality has been mered into the regular custom model
    - Along with this, there is now only a single `.register` method in the event, the previous registration methods are now deprecated
  - TFC climate default accessors have been moved from the event to the model builder callback
  - The wind vector callback now provides a `Level` and `BlockPos` instead of a `BlockContainerJS`
  - Custom noises are now created differently:
    - First, `.newNoise` is called with a callback where the noise can be modified, this returns an index
    - This index can be used in `.noise` to get the noise within calculation callbacks
- Spreading bush blocks are now tagged with `tfc:any_spreading_bush` by default, fixing max height being ignored and cane blocks breaking
- Add methods for setting the model and textures of bush blocks
- Cane block models are now named `<parent_id>_side_<lifecycle>_<stage>` to match TFC's model naming scheme, previously they were named `<parent_id>_cane_side_<lifecycle>_<stage>`
- Fix the use of `productItem` with a pre-existing item not working as it should
- Add `/kubejs_tfc search` command, enabling pack makers to determine which data entries are applied to an item/block/fluid

### 1.2.4
- Fix FirmaLife cheese wheel block builders sharing inside textures
- Fix potential crashes & inconsistencies related to custom entity type's hand & armor items (Thanks Liopyu!)
- Fix custom path navigations not working for custom entity types, update EntityJS version in-dev (Thanks Liopyu!)
- Add the ability to create custom glass operations
- Fix custom horizontal support block loot tables
- Add `TICKS_IN_HOUR`, `HOURS_IN_DAY`, `TICKS_IN_DAY`, `MONTHS_IN_YEAR`, and `TICKS_IN_MINUTE` fields to calendar bindings
- Remove deprecated methods in fauna registration, ISM, rock settings, and worldgen data events
- Add independent `getPos()` method to events where it makes sense to access

### 1.2.3
- Add `tfc:jug` and `tfc:glass_bottle` item types, similar to the `tfc:fluid_container` item type, but can be drunken from
- De-hardcode most non-crop resource generation
- Add `/kubejs_tfc list_ids` and `/kubejs_tfc describe` commands which can be used to print info about some of TFC's data types

### 1.2.2
- Add Beneath nether fertilizer and lost page builders to the `TFCEvents.data` event if it is installed
- Register the previously missing dispenser behaviors for custom `tfc:fluid_container` items
- Fix the `#withPreExisting` method not working as intended in custom ground cover blocks
- Yet more custom crop fixes (hopefully for the last time)

### 1.2.1
- Fix custom fauna registrations causing *certain mods* to crash because they do not use Forge's event
- Changes to `TFCEvents.registerFaunas`
  - Deprecate `.register`, identical function provided by `.replace`
  - Add `.replace`, `.and`, and `.or` methods, these perform the operation their name implies on any existing spawn placements with the fauna based on created through it
    - Have the exact same args as the now-deprecated `.register` method
    - Also have a version with a nullable string arg added after the entity type, this is suffixed to the registered fauna's id with a `/`
- Fix not being able to define portions in the meal ISP modifier
- Fix custom crops having an empty loot table by default

### 1.2.0
- Fix custom lamps not retaining their fluid when broken
- Register custom blocks with block entities to TFC's own block entities instead of creating a duplicate block entity type
- Custom grass, farmland, and path blocks now use their parent dirt block's texture as their base by default
- Add an event for modifying default worldgen values, including the world's rock layers
  - Change `TFCEvents.rockSettings`'s method from `defineLayer` to `defineRock` to match terminology used here
- Add axle block builder, which has methods for creating corresponding windmills, water wheels, gear boxes, clutches, and bladed axles
- Add encased axle block builder
- Add extremely basic [EntityJS](https://modrinth.com/mod/entityjs) compat
  - Has `mammal`, `oviparous`, `wooly_animal`, and `dairy_animal` types currently
- Add event for registering a fauna definition to any entity type in the game
- Add methods for getting the fuel, drinkable, fertilizer, lamp fuel, pannable, and sluicable definitions to the misc sub-binding
- Update to TFC 3.2.6 and FirmaLife 2.1.6
- Add recipe handlers for new FL recipe types
- Allow custom windmill blades to specify the texture they use
- Non-wild crops now no longer have an item by default
- Now use Kube's `dev.properties` file instead of a config
  - `debugMode` -> `debugInfo` (default property)
  - Add `tfc/insertSelfTestsIntoConsole` property that will put TFC's self tests into the Kube console if enabled (defaults to true)

### 1.1.3
- Add anvil block builder
- Groundcover block builders can now use preexisting items as their item similar to TFC's sticks
- Custom farmland and connected grass blocks now have some of TFC's functional tags by default
- Added event for registering custom 'interactions', see the wiki page for `TFCEvents.registerInteractions`
- Fully remove legacy container limiting system/event
- Add more methods in the misc sub-binding
- Update to TFC 3.2.3

### 1.1.2
- Fix `TFC.misc.rock` and `TFC.misc.rock` maps being empty if called too soon

### 1.1.1
- Add `tfc:inventory` BE attachment, identical to kube's default inventory attachment but allows for filtering allowed items by TFC size & weight
- Fix the register representatives event, now takes a `Block[]` instead of a `List<ResourceLocation>`
- Fix the `replacementMap` param in the soil disc worldgen data event's JSDoc
- The `TFC.misc.wood` object is now a `Map<String, NamedRegistryWood>`, identical to before but the returned objects now have a `getMod()` method which returns the mod they originate from
- Fix events that should have `cancel()` called in them
- Add some methods to miscellaneous bindings
- Fix crop builders' seed items
- Fluid and block ingredient js builders can now take regex inputs
- Update to TFC 3.2.1
- Add custom support block and windmill blade item builders
- Add sewing recipe support

### 1.1.0
- Update to TFC version 3.1.9
- Update to KubeJS version `2001.6.4-build.121`+, fix collapse, landslide, and chisel recipes being broken with said kube versions
- Add misc sub-binding
  - Has many things! Check the wiki
- Events for custom birthdays, item stack modifiers, and adding prospecting representative blocks
- Add item builders
  - TFC fishing rod
  - Precision Prospecting's item types
  - Jars
  - Glassworking items
  - FirmaLife's watering can
- Add block builders
  - Lamp
  - FirmaLife's cheese wheel
  - Stationary berry bushes
  - Spreading berry bushes
  - Dirt blocks which will also create a grass block and optionally a path, farmland, and rooted dirt variant
  - Crops
    - Wild
    - Spreading
    - Double
    - Default
    - Flooded
    - Pickable
- Rework container limiting functionality
- Fix type description for isp components
- `TFC.isp` can now be used in place of `TFC.itemStackProvider` to access item stack provider bindings
- Add methods for FirmaLife's isp modifiers to `ItemStackProviderJS` if FL is present
- Add methods to add custom molds to TFC Casting With Channels' mold table if TFCCC is present
- Add recipe handlers for FirmaLife and ArborFirmaCraft if they are present
- Update custom javelin models
- Add support for more feature types in the worldgen data event
- Ship method param names for ProbeJS hints

### 1.0.3
- Update to TFC 3.1.3-beta
- Remove config option to disable Kube's sync recipes (it didn't seem to work anyway)
- Rework how custom food traits are made
- the `tfc:raw_rock` block builder should now not complain about missing mirrored models
- Fix fertilizers adding `phosphorous` instead of `phosphorus`
- Add missing `use_durability` and `chance` parameters to heating recipes

### 1.0.2
- Fix an issue with delegate recipe types attempting to reference their id when generating their id
- Add douse fire event
- Rework how rock layers are defined
  - The event has been renamed from `registerRockSettings` to just `rockSettings`
  - The `defineLayer` method now takes each block individually instead of altogether as a consumer
  - `defineLayer` now returns the `RockSettings` object created

### 1.0.1
- Initial 1.20.1 port
  - Requires TFC 3.1.2-beta and KubeJS 2001.6.4-build.95
- Port recipes to new schema system
  - Delegate crafting recipe types no longer accept the same arguments as their base crafting type, now only support a single recipe argument
- Block, item, and fluid (stack) ingredients now use TFC's own native classes
- Rework bindings
  - Going forward everything provided by this mod will be accessible through the `TFC` object or, for events, the `TFCEvents` object
- Added JSDoc annotations to everything, meaning ProbeJS should have explanations in its hints
- Split custom spring water healing and particle stuff into its own fluid builder type
- Add common config to automatically turn off async recipes at instance start to prevent problems with TFC's knapping and alloy recipe types
  - **Note**: This does not edit the file and only applies once, reloading KubeJS' common config (i.e. with one of KubeJS' reload commands) will revert the instance back to the setting present in the file
  - This problem should theoretically be resolved in a future TFC release, see my recent chats w/ Alc in the #development channel of TFC's discord
- Add `tfc:hammer` custom item type
- Minimize usage of consumers where possible
- Events
  - Custom rock layers can still be created, but they cannot be added to the world, nor can existing ones be edited or removed anymore
  - Custom climate models now have a callback for the wind vector, callbacks referencing TFC's default implementation can now be accessed
  - Fully remove custom data and worldgen feature creation from Kube's datapack events
- Known (possible) problems/ untested things
  - The autogenerated models for custom javelins may not entirely correct
  - The [wiki](https://notenoughmail.github.io/kubejs_tfc/1.20.1/) does not currently contain some information, but the pages for recipes and data (and worldgen soon hopefully) should be accurate
    - I will work on it when I have the time
  - Moss growing block types are not fully tested
  - Custom fluid containers filled translation component has not been tested
  - TFC's crafting recipe types do not have any way to set the `mirror`, `group`, or `conditions` parameters at this moment

### 0.6.2
- Fix data events data being added too late for metals and worldgen to be useful in-world

### 0.6.1
- Add `tfc.data` and `tfc.worldgen.data` events to replace a mixin into `DataPackEventJS`
- Deprecate the data builder methods currently present in the `server.datapack.*` events, they are now available in the above mentioned events
- Add the ability to register custom food traits
- Fix potential problems with certain client-possible events trying to call server code

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