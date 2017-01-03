<!DOCTYPE html>
<html>
	<head>
		<title>Quark</title>

		<meta name="theme-color" content="#48ddbc">
		
		<link rel="stylesheet" href="https://code.getmdl.io/1.1.3/material.teal-orange.min.css">
		<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

		<link rel="stylesheet" href="quark.css">
		<link rel="icon" href="favicon.ico">
	</head>

	<body>
		<div id="main-container">
			<div id="header">
				<img src="img/quark.png" id="quark-logo"></img>
				<a href="https://www.creeperhost.net/"><img id="sellout-image" src="http://vazkii.us/sellout.png"></img></a>

				<div id="header-contents">
					<div class="title"><span class="q">q</span>uark</div>
					<div class="subtitle">a big mod made of small things.</div>
				</div>

				<button id="download-button" class="mdl-button mdl-js-button mdl-button--fab mdl-button--colored" onclick='window.open("http://minecraft.curseforge.com/projects/quark")'>
					<i class="material-icons">file_download</i>
				</button>
				<div id="download-section">
					<img src="http://cf.way2muchnoise.eu/full_quark_downloads.svg"></img> <img src="http://cf.way2muchnoise.eu/versions/quark.svg"></img>
				</div>
			</div>

			<div id="contents" class="mdl-layout mdl-js-layout mdl-layout--transparent">
				<header class="mdl-layout__header mdl-layout__header--transparent">
					<div class="mdl-layout__tab-bar mdl-js-ripple-effect mdl-layout--fixed-tabs mdl-layout__header--transparent">
						<a href="#module-info" class="mdl-layout__tab is-active">Info</a>
						<a href="#module-automation" class="mdl-layout__tab">Automation</a>
						<a href="#module-building" class="mdl-layout__tab">Building</a>
						<a href="#module-decoration" class="mdl-layout__tab">Decoration</a>
						<a href="#module-management" class="mdl-layout__tab">Management</a>
						<a href="#module-tweaks" class="mdl-layout__tab">Tweaks</a>
						<a href="#module-vanity" class="mdl-layout__tab">Vanity</a>
						<a href="#module-world" class="mdl-layout__tab">World</a>
					</div>
				</header>

				<div class="mdl-layout__content">
					<section class="mdl-layout__tab-panel is-active" id="module-info">
						<div class="page-content">
							<b>Quark</b> is a mod for Minecraft. It aims to create an experience akin to the "vanilla" experience, by having a very simple motto: <i>anything that would be added to Quark could also be added to the default game without compromising its gameplay style.</i> Quark currently has <b id="feature-counter"></b> features to spice up your game, all of which fitting the previous motto.
							<br><br>
							To that end, every feature added to Quark is simple and small. Hence the name of the mod, Quark, as a Quark is a very small thing. Quark is a <i>Modular</i> mod. Which means that its features are split within various modules. All of the modules can be disabled, and all the features can also be disabled individually if one wants to. Many of the features even have extra configuration options.
							<br><br>
							The mod features ingame configuration via the Mod Options menu. Most features can be toggled from inside the game, others require a world reload, it depends on the changes they make. Any features that add new explicit content, such as items, blocks or entities, will require a reset.
							<br><br>
							Check out the various tabs to have a look of what the mod contains. If you're on mobile you can scroll left on the tabs to see more. You can download Quark using the download button in the header. Scroll down a bit for installation instructions and other stuff.
							<br><br>
							<b>Note that this mod requires <a href="https://minecraft.curseforge.com/projects/autoreglib">AutoRegLib</a>, it must be installed for it to work.</b> (older versions of the mod may not require it)
							<br><br>
							<img id="main-image" src="img/main.png"></img>

							<hr>

							<b>Installation Instructions</b>
							<br><br>
							This mod requires Minecraft Forge. We do not support Bukkit, Spigot, Cauldron, Sponge or any other alternative servers. Though some <i>may</i> be compatible, if you choose to go that way, you're on your own. <i>The mod is provided as-is, with no warranty. We are not responsible if it breaks or destroys your save. Always make backups.</i>
							<br><br>
							This is just a standard forge mod installation. If you've done it before you can skip this.
							<ol>
								<li>Make sure you have Minecraft installed.</li>
								<li>Locate the version of Minecraft Forge for your target Minecraft version <a href="http://files.minecraftforge.net/">here</a>.</li>
								<li>Download installer-win (or installer if you're not on Windows) and run it. A new forge profile will be added to your minecraft launcher.</li>
								<li>Download AutoRegLib using the link above the image. Put the downloaded .jar into your "mods" folder.</li>
								<li>Download Quark using the download button above. Put the downloaded .jar file into your "mods" folder.
									<ul>
										<li>The "mods" folder is next to your "resourcepacks" folder. To locate it, run the new forge instance, open the Resource Packs menu, open the folder, and go one level up to where all your minecraft files are. The "mods" folder should be there alongside others like "screenshots" or "saves".</li></ul></li>
								<li>Run your new forge profile with Quark installed.</li>
							</ol>

							For server installation:
							<ol>
								<li>Follow the above steps, up to step 3. For step 3 pick Server rather than Client, and change the location to an empty folder somewhere.</li>
								<li>Create a "mods" folder inside the empty folder you picked, that now contains your server files.</li>
								<li>Download AutoRegLib using the link above the image. Put the downloaded .jar into that "mods" folder.</li>
								<li>Download Quark using the download button above. Put the downloaded .jar file into that "mods" folder.</li>
								<li>Run the server using the forge jar. <i>Not the minecraft_server jar</i></li>
							</ol>

							Alternatively, you can use the <a href="https://www.curse.com/download">Curse app</a> and install the mod directly from inside it. Make sure you have Minecraft installed so it installs the Minecraft plugin.<br>
							Yes, you can use the mod in modpacks.
							<hr>
							<b>Credits</b>
							<ul>
								<li>wiiv for most of the textures.</li>
								<li>Rorax for the emote icons.</li>
								<li>DylanKaiser for the inventory chest icon.</li>
								<li>SanAndreasP for the chest textures and most of their code.</li>
								<li>Darkhax for the sheep having wool feature.</li>
								<li>ZeroLevels for the Iron Plate texture.</li>
								<li>Noodlor for the variety dungeon structures.</li>
								<li>/u/Soniop for the realistic world preset.</li>
								<li>/u/Martwaza for the trapdoor textures.</li>
								<li>/u/FelitonC and /u/origamidragon412 for the banner textures.</li>
								<li>/u/kopasz7 for the Midori block textures.</li>
								<li>/u/robotthunder500 for the Bookshelf textures.</li>
								<li>/u/darwinpatrick for the Soul Sandstone textures.</li>
								<li>The creators of all the awesome suggestions I found in /r/MinecraftSuggestions that I implemented here.</li>
							</ul>
							<hr>
							<b>Contact and Donations</b>
							<ul>
								<li><a href="https://www.reddit.com/r/minecraftsuggestions">Minecraft Suggestions Subreddit</a> <i>(suggest here, tag /u/vazkii in a comment if you want it in quark)</i></li>
								<li><a href="https://github.com/Vazkii/Quark/issues">Issues</a> <i>(report your bugs here)</i></li>
								<li><a href="https://github.com/Vazkii/Quark">Source Code</a></li>
								<li><a href="https://github.com/Vazkii/Quark/blob/master/modders.md">APIs for Modders</a></li>
								<li><a href="https://twitter.com/Vazkii/">Vazkii's Twitter</a></li>
								<li><a href="https://patreon.com/Vazkii/">Vazkii's Patreon</a></li>
								<li><a href="https://twitter.com/wiliv75/">wiiv's Twitter</a></li>
								<li><a href="https://patreon.com/WIIV/">wiiv's Patreon</a></li>
								<li><a href="http://vazkii.us/about-me/supportdonations/">Donations</a></li>
								<li><a href="http://vazkii.us/about-me/contact/">Email and other Contacts</a></li>
								<li><a href="http://vazkii.us/">vazkii.us</a></li>
							</ul>
							<br>
	 					</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-automation">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">Automation</div>
								<div class="module-desc">
									This module adds features to minecraft's automation prospects.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/automation/dispensers.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Dispenser Planting</div>
									<div class="feature-desc">
										Dispensers can plant seeds on the correct type of soil.<br><br>
										This works for all overworld seeds, on Tilled Soil, Nether Wart, on Soul Sand, and Chorus Flower on End stone. Cocoa Beans can be planted if there's a Jungle Wood block in front of the dispenser.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/automation/obsidian-plates.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Obsidian Pressure Plates</div>
									<div class="feature-desc">
										Pressure Plates made with obsidian will only trigger when Players walk over them.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/automation/piston-block-breakers.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">End Rods as Block Breakers</div>
									<div class="feature-desc">
										Pistons that push an End Rod will have it work as a drill of sorts, breaking any blocks in front in the direction the rod is facing.<br><br>
										These can break anything the piston can push.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/automation/weather-sensor.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Weather Sensor</div>
									<div class="feature-desc">
										The Weather Sensor is crafted with Purpur Slabs, Biotite (from the World module, or Obsidian if not enabled) and Glass. It works like a Daylight Sensor, but it emits a full redstone signal if it's raining or snowing.<br><br>
										Right clicking it inverts its output.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/automation/ender-watcher.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Ender Watcher</div>
									<div class="feature-desc">
										The Ender Watcher is a new redstone input block. It emits a full redstone signal if a player is looking directly at it.<br><br>
										It's crafted with an Eye of Ender, Redstone and Blocks of Biotite (from the World module, or Obsidian if not enabled).
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/automation/floor-food-eating.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Hungry Animals</div>
									<div class="feature-desc">
										Animals that have a breeding food assigned to them will eat it from the floor at any time they can have it fed manually, provided the food is nearby.
									</div>
								</div>
							</div>
						</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-building">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">Building</div>
								<div class="module-desc">
									This module adds new structural building blocks.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/clay-tiles.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Hardened Clay Tiles</div>
									<div class="feature-desc">
										"Tile" style blocks for Hardened Clay and all colors of Stained Clay. They can all be turned into stairs and slabs, and look great for roofs.<br><br>
										4 Blocks are crafted with a 2x2 of the respective Hardened/Stained Clay block.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/reed-blocks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Sugar Cane Blocks</div>
									<div class="feature-desc">
										A 3x3 of Sugar Canes creates a Sugar Cane Block. These can be turned into stairs, slabs and walls. You can also turn them back into Sugar Canes, if you want.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/sandy-bricks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Sandy Bricks</div>
									<div class="feature-desc">
										Bricks with a sandy tone, crafted with a Brick block and a Sand Block. They can also be used for stairs, slabs, and walls.
									</div>
								</div>
							</div>


							<div class="feature">
								<img class="feature-image" src="img/building/charred-nether-bricks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Charred Nether Bricks</div>
									<div class="feature-desc">
										Charred Nether Bricks bring a slate-like tone for a dark but not black block. Crafted with a Nether Brick (block) and a Fire Charge. They can also be used for stairs and slabs.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/snow-bricks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Snow Bricks</div>
									<div class="feature-desc">
										Snow Bricks, for all your igloo and winter wonderland needs. They can also be used for stairs, slabs, and walls.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/thatch.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Thatch</div>
									<div class="feature-desc">
										Thatch is created via a 2x2 of Wheat. It can be used for stairs and slabs or turned back into 4 Wheat.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/midori.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Midori Blocks</div>
									<div class="feature-desc">
										Midori Blocks are a green counterpart to Purpur. These are made with Cactus Green, with the same recipes as the Purpur blocks.<br><br>Midori means "green" in Japanese.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/stone-bricks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">More Stone Bricks</div>
									<div class="feature-desc">
										Stone Bricks for Granite, Diorite and Andesite. Crafted with a 2x2 of the Polished variant. All of them can be used for stairs, slabs, and walls.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/bark-blocks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Bark Blocks</div>
									<div class="feature-desc">
										Log-type blocks with the log "bark" texture on all sides. Crafted with a 2x2 of the respective log. Can be turned into walls.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/polished-stone.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Polished Stone</div>
									<div class="feature-desc">
										Created with 2 Stone Slabs, similarly to Chiseled Sandstone. Looks like the top of the Stone Slab texture on all sides.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/vertical-planks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Vertical Wooden Planks</div>
									<div class="feature-desc">
										Putting 3 wooden planks in a vertical line creates 3 Vertical Wooden Planks. Doing the same reverts it.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/carved-wood.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Carved Wood</div>
									<div class="feature-desc">
										Carved Wood is a cornerstone type block for wood, made with a 2x2 of the respective wood slab.
									</div>
								</div>
							</div>
							
							<div class="feature">
								<img class="feature-image" src="img/building/iron-plates.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Iron Plates</div>
									<div class="feature-desc">
										Iron Plate blocks can be created with 8 Iron Ingots in the donut shape, this recipe outputs 24 plates. Iron Plates can also be turned into stairs and slabs.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/soul-sandstone.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Soul Sandstone</div>
									<div class="feature-desc">
										Sandstone made of Soul Sand. All recipes are as expected.<br><br>
										Soul Sandstone can be turned into Stairs, Slabs, and Walls.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/new-sandstone.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">More Sandstone</div>
									<div class="feature-desc">
										(Red) Sandstone was begging for more variants, so now we have Polished Sandstone and Sandstone Bricks. Polished Sandstone is crafted with a square shape of Sandstone, whereas Sandstone Bricks are crafted with the standard brick recipe using Polished Sandstone. Of course these recipes are the same for Red Sandstone.<br><br>
										Polished Sandstone can be used for slabs, whereas Sandstone Bricks can be used for slabs and stairs.<br><br>

										(not pictured, variants for Soul Sandstone as well, if it's enabled)
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/building/new-variants.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Stairs, Slabs, and Walls</div>
									<div class="feature-desc">
										Stairs, Slabs, and Walls for a bunch of vanilla materials: Stone, Granite, Diorite, Andesite, End Bricks, Prismarine, Prismarine Bricks, and Dark Prismarine.
										<br><br>Walls for, aside from all the previous: Purpur, Bricks, Quartz, Sandstone, Red Sandstone, and Stone Bricks.
										<br><br>As of 1.10, Red Nether Bricks also get Stairs and Slabs.
									</div>
								</div>
							</div>
						</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-decoration">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">Decoration</div>
								<div class="module-desc">
									This module adds new decorative building blocks.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/lit-lamp.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Lit Redstone Lamp</div>
									<div class="feature-desc">
										Combining a Redstone Lamp with a Redstone Torch creates a Lit Redstone lamp, that's always emitting light.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/blaze-lantern.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Blaze Lantern</div>
									<div class="feature-desc">
										The Blaze Lantern is the fire equivalent of the Sea Lantern. Crafted with a + of Blaze Powder and Blaze Rods in the corners, like a Sea Lantern.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/charcoal-block.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Block of Charcoal</div>
									<div class="feature-desc">
										It's a Block of Charcoal. Made with 9 Charcoal. Not sure what you expected here.<br><br>
										Similarly to Netherrack, this block retains fire.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/paper-walls.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Paper Walls</div>
									<div class="feature-desc">
										Paper Walls use the "pane" look, which lets you make fancy oriental style buildings. Thanks to the new 1.9 change that makes standalone panes look like poles, they can also be used to make makeshift "tiki torches".<br><br>
										These are crafted with 3 Paper in the middle row, and Sticks covering the rest of the grid.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/leaf-carpet.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Leaf Carpet</div>
									<div class="feature-desc">
										Leaf Carpet can be made with all types of leaves in the same recipe as wool carpet. It can be harvested by hand and has no collision box.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/trapdoors.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">All The Trapdoors</div>
									<div class="feature-desc">
										This allows you to make trapdoors of all wood blocks rather than just Oak. It also changes the recipe to create 6 rather than 2 trapdoors, since doors make 3.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/bookshelves.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">All The Bookshelves</div>
									<div class="feature-desc">
										You can now make Bookshelves out of all wood types. All of these bookshelves work for the Enchanting Table too.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/chests.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">All The Chests</div>
									<div class="feature-desc">
										Just like trapdoors and bookshelves, this allows you to make chests of all wood types. It also adds a recipe to make 4 chests at once, using 8 wood logs of the respective type in the same donut pattern.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/nether-brick-fence-gate.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Nether Brick Fence Gate</div>
									<div class="feature-desc">
										A Fence Gate made of Nether Bricks. Crafted with Nether Brick blocks in the place of planks and Nether Brick Fences in the place of Sticks. The recipe outputs 2.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/iron-ladders.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Iron Ladders</div>
									<div class="feature-desc">
										Iron Ladders can be crafted with the H shape of Iron Ingots. They work like normal ladders, except that they can stand by themselves as long as there is an Iron Ladder on top of them, like in the picture.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/colored-beds.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Colored Beds</div>
									<div class="feature-desc">
										Beds made with different types of wool will have that wool color. If you mix wool types you'll get a white bed instead.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/colored-item-frames.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Colored Item Frames</div>
									<div class="feature-desc">
										Item Frames can be combined with any Dye to color them. All functionality remains the same.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/decoration/banner-icons.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">New Banner Motifs</div>
									<div class="feature-desc">
										New banner motifs can be made using Dragon Heads, Eyes of Ender, Iron Swords, and Iron Chestplates, alongside the chosen color of dye.
									</div>
								</div>
							</div>
						</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-management">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">Management</div>
								<div class="module-desc">
									This module adds inventory management features.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/management/f-swap.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">F to Switch</div>
									<div class="feature-desc">
										Pressing F on an item in the inventory swaps it with your off-hand item, just as if you'd pressed F outside of the inventory. This uses the same keybind used for that purpose.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/management/dropoff.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Dropoff Button</div>
									<div class="feature-desc">
										A "Dropoff" button is added to the inventory screen. Pressing this button deposits everything in your inventory (not hotbar, armor or off-hand) in nearby chests. The range it stores in is similar to the player's reach, so it won't store items in chests the player can't open. The closest chests are picked first.<br><br>
										If Shift is held, it stores items in "smart" mode, meaning it'll only store items in chests that already have at least one of that item.<br><br>
										<i>This feature can be disabled from clients using /gamerule quark_allowDropoff false</i>
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/management/sorting-button.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Sorting Button</div>
									<div class="feature-desc">
										A "Sort" button is added to the inventory screen. Pressing this button sorts your inventory, excepting your hotbar.<br><br>
										Sorting is done via a category system, in which items are placed based on what category they fit in (food, tool, armor, block, etc.). Each category also has its own sorting rules, e.g. food will get sorted based on which is more filling; tools will get sorted based on which one is the best, or has more durability.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/management/chest-buttons.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Chest Buttons</div>
									<div class="feature-desc">
										Three buttons are added to Chest interfaces. From top to bottom, they do the following:
										<ul>
											<li>(not pictured) Sort the chest, if the Sorting Button feature is enabled.</li>
											<li>Extract all items from the chest that you have at least one of in your inventory.</li>
											<li>Deposit your entire inventory (not hotbar, armor or off-hand) in the chest.</li>
											<li>Same as the previous, but in "smart" mode.</li>
										</ul>
									</div>
								</div>
							</div>
							
							<div class="feature">
								<img class="feature-image" src="img/management/favorites.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Favorite Items</div>
									<div class="feature-desc">
										Alt-clicking an item will make it a "Favorite Item". Favorite items are not deposited by the Dropoff button or the last two Chest buttons.
									</div>
								</div>
							</div>
							
							<div class="feature">
								<img class="feature-image" src="img/management/delete-items.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Delete Items</div>
									<div class="feature-desc">
										Pressing Ctrl+DELETE while hovering over an item will delete that item from your inventory. The item is deleted forever, this can't be undone.<br><br>
										<i>How do you even make an image for this?</i>
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/management/link-items.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Item Linking</div>
									<div class="feature-desc">
										Pressing Shift+T (or whatever your Chat key is set to) while looking at an item in an inventory links that item to chat. Other players can hover over it to check it out.
									</div>
								</div>
							</div>
						</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-tweaks">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">Tweaks</div>
								<div class="module-desc">
									This module tweaks various gameplay elements.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/angry-creepers.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Angry Creepers</div>
									<div class="feature-desc">
										Creepers turn red when they're about to explode.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/safe-horses.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Safe Mounts</div>
									<div class="feature-desc">
										Mounts are immune to attacks from riders. No more shooting your own horse!
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/mount-hud.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Improved Mount HUD</div>
									<div class="feature-desc">
										While on a mount, your food display is still shown. Furthermore, while on a horse, the jump bar is only shown while holding down the jump key, the XP bar is shown otherwise.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/chicken-shed.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Chicken Shedding</div>
									<div class="feature-desc">
										Chickens shed feathers periodically, similarly to how they lay eggs.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/chicken-shear.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Chicken Shearing</div>
									<div class="feature-desc">
										Chickens can be sheared. Doing so has them take 1 damage and drop 1 feather. You can do this as many times as you want while the chicken doesn't die. Chickens that have been sheared will not drop feathers upon death.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/sheep-armor.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Sheep Armor</div>
									<div class="feature-desc">
										Sheep that have wool also have a bit of armor added to them, making them take less damage.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/endermites-into-shulkers.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Endermites into Shulkers</div>
									<div class="feature-desc">
										Endermites will rarely bury into Purpur Blocks, much like how Silverfish bury into stone. When they do, they fuse with the block, forming a Shulker.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/glass-shards.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Glass Shards</div>
									<div class="feature-desc">
										Glass drops shards. Stained Glass will drop shards of the respective type. Crafting them in a 2x2 creates a Glass block back.<br><br>
										The shards act like Glowstone Dust, in which Fortune will let you get more, and Silk Touch will just drop the block.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/drop-down-ladders.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Ladder Drop Down</div>
									<div class="feature-desc">
										Right clicking a ladder with another ladder will place it under the one right clicked, as long as there is space for it.<br><br>
										This allows for ladders to be properly extended downwards without finnicky shift-click managing.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/look-down-ladders.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Ladder Sliding</div>
									<div class="feature-desc">
										When on a ladder, if not moving or sneaking, looking down will have you go down the ladder faster.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/slabs-to-blocks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Slabs to Blocks</div>
									<div class="feature-desc">
										Slabs can be converted back to their respective blocks. These recipes are generated dynamically for every slab type in the game.<br><br>
										In the case that there's already a recipe of this sort (like Chiseled Sandstone), that recipe will be the one used for crafting.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/stairs-to-blocks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Stairs to Blocks</div>
									<div class="feature-desc">
										Stairs can be converted back to their respective blocks. Like the previous feature, these recipes are generated dynamically for every stair type in the game.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/more-stairs.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Proper Stair Math</div>
									<div class="feature-desc">
										Stairs make 8 rather than 4. Why did they even make 4 in the first place? How does that even work? Anyway, this modifies every stair recipe in the game.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/stackable-carts.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Stackable Minecarts</div>
									<div class="feature-desc">
										All types of Minecarts can be stacked up to 16.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/flint-tools.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Flint Tool Crafting</div>
									<div class="feature-desc">
										Stone Tools can be crafted with Flint. The standard cobblestone recipe is untouched.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/clay-conversion.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Clay Conversion</div>
									<div class="feature-desc">
										Clay blocks can be converted into the Clay item via crafting.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/burning-torches.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Burning Torches</div>
									<div class="feature-desc">
										Torches can be burnt in furnaces for 2 items each.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/cart-interactions.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Minecart Block Placement</div>
									<div class="feature-desc">
										Right clicking a normal minecart with a block that can go in it (Chest, Hopper, etc) will turn the normal cart into that type of cart.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/hoe-sickle.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Hoe Harvesting</div>
									<div class="feature-desc">
										Breaking grass or crops with a hoe will break a 3x3 of them. A Diamond Hoe will break a 5x5 instead.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/shave-snow.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Shave Snow Layers</div>
									<div class="feature-desc">
										Right clicking a block of snow (full block or layered block) with a shovel will remove a layer from it.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/jump-boost.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Jump Boost Step Assist</div>
									<div class="feature-desc">
										Having Jump Boost II on your character will allow you to go up 1 block high gaps, like Horses can.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/autojump-keybind.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Auto-jump Toggle Keybind</div>
									<div class="feature-desc">
										Pressing the B key will toggle your auto-jump feature. A small HUD element will show up telling you whether it's currently active or not.
										<br><br>
										This is just a normal keybind, so you can change it to whatever you want.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/no-potion-shift.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">No Potion Shift</div>
									<div class="feature-desc">
										Prevents potion effects from shifting your inventory to the side.<br><br>
										Does literally the same as the mod of the same name. It has the same one line of code even!
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/double-doors.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Double Door Opening</div>
									<div class="feature-desc">
										Double doors open together when right clicked.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/knock-knock.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Knock on Doors</div>
									<div class="feature-desc">
										Left clicking a door with an empty hand will have you "knock". This makes a bit of a louder sound than if you left clicked any other block, but nowhere as infuriating as opening and closing it.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/breath-bottle.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Effective Dragon's Breath</div>
									<div class="feature-desc">
										Dragon's Breath will not leave behind a bottle when brewed with, so it doesn't mess up all brewing automation systems.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/food-tooltip.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Food Tooltip</div>
									<div class="feature-desc">
										Food items show how much they heal in their tooltip.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/snowman-heads.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Snow Golem Heads</div>
									<div class="feature-desc">
										Named Snow Golems equipped with a Pumpkin will drop a player head with the same name as theirs, if killed by a Witch.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/unintrusive-shield.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Subtler Shields</div>
									<div class="feature-desc">
										Shields have been pushed back a bit, so they don't use up as much of your screen when equipped and not blocking.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/note-blocks.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Monster Note Blocks</div>
									<div class="feature-desc">
										Note Blocks with heads attached to their side will emit the respective mob's sound instead of a note.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/armor-stand.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Armed Armor Stands</div>
									<div class="feature-desc">
										Armor Stands placed by the Armor Stand item will have arms.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/burning-baby.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Burning Baby Zombies</div>
									<div class="feature-desc">
										Baby Zombies will die in sunlight like their parents.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/sign-edit.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Sign Editing</div>
									<div class="feature-desc">
										Right Clicking a Sign lets you edit it.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/ender-cheese.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Enderman Anti-Cheese</div>
									<div class="feature-desc">
										Going under a 2 block high area while being attacked by an enderman will have the enderman teleport <i>you</i> to it. This ability is negated in Easy mode.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/texture-changes.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Texture Changes</div>
									<div class="feature-desc">
										Quark generates a "low priority resource pack", changing the 1.8 stones, bricks, glass, and pupkin face textures to less rough or more sensible variants.<br>
										Each of these can be turned off individually, and will be overriden by any other resource pack you load on top.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/tweaks/greener-grass.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Greener Grass</div>
									<div class="feature-desc">
										Grass is greener on the Quark side. Biome colors are still respected, but are greener non the less.<br><br>
										There's also config options to enable Alpha style grass, which gives you the Minecraft Alpha vibe, or to manually set the grass color.
									</div>
								</div>
							</div>
						</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-vanity">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">Vanity</div>
								<div class="module-desc">
									This module adds vanity features for your character.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/dyable-elytra.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Dyable Elytra</div>
									<div class="feature-desc">
										The Elytra can be dyed with any of the 16 dyes. You can dye it as many times as you want.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/witch-hat.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Witch Hats</div>
									<div class="feature-desc">
										Witches have a rare chance to drop their hat on death (same as a Wither Skeleton Skull drop).<br><br>
										Aside from being an awesome fashion accessory, the hat provides leather level armor, high enchantability and a built in 50% resistance to potions of harming from witches.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/boat-sails.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Boat Sails</div>
									<div class="feature-desc">
										Right clicking a Boat with a Banner adds it as a Sail. The sail is decorative only and is dropped when the boat is destroyed.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/benches.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Stair Chairs</div>
									<div class="feature-desc">
										Right clicking a stair block facing up, with empty hands, will let you sit down on it.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/firework-dupe.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Firework Duplication</div>
									<div class="feature-desc">
										Empty Fireworks can be crafted with already made ones to duplicate them. This works like Banners, in which the origin firework will not get used up.<br><br>

										Both fireworks need to have the same flight duration for this to work.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/dyed-item-names.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Dyed Item Names</div>
									<div class="feature-desc">
										Combining an item with a dye in an anvil has the item's tooltip name take that dye's color. This costs 3 levels.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/emotes.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Emotes</div>
									<div class="feature-desc">
										In the corner of the Chat menu there's a button to open an emote panel with 10 different emotes your character can do. In this picture, Alex is doing the "Wave" emote.<br><br>
										Each emote is also assigned a key binding. Pressing that key will do the emote without you having to open the chat menu. By default all emote keybinds are unset and must be set manually.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/vanity/panorama-maker.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Panorama Maker</div>
									<div class="feature-desc">
										Holding Ctrl and Shift (or Cmd and Shift in a Mac) while taking a screenshot takes a "Panorama Screenshot". Panorama screenshots are split into 6 low resolution images and can be used for resource packs to change the title screen look.<br><br>
										Furthermore, when loading the game, a random panorama screenshot the player has taken will be chosen to replace the vanilla panorama in the title screen. Panorama screenshots are stored in a "panoramas" folder under the "screenshots" folder, and sorted by folders. Renaming one of the inner folders to "main_menu" will make that image always show up in the title screen.
									</div>
								</div>
							</div>
						</div>
					</section>

					<section class="mdl-layout__tab-panel" id="module-world">
						<div class="page-content">
							<div class="module-info">
								<div class="module-title">World</div>
								<div class="module-desc">
									This module adds world generation features.
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/stone-revamp.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Stone Revamp</div>
									<div class="feature-desc">
										Diorite, Granite, and Andesite will now spawn in really large, uncommon clusters, rather than all over the place in small bits. Furthermore, two new stones are added to the mix: Marble and Limestone (pictured). These last two can be used for anything the first three can.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/basalt.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Basalt</div>
									<div class="feature-desc">
										Basalt spawns in the Nether in large clusters. It works like the overworld rocks (Granite, Diorite, and Andesite), and the recipes for it are similar.<br><br>
										Combining Basalt with Quartz makes Andesite. Combining Cobblestone with Biotite (Coal if not enabled) makes Basalt.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/biotite.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Ender Biotite</div>
									<div class="feature-desc">
										Ender Biotite is a renewable ore that does not spawn naturally. Clusters of it will, instead, get scattered across the main End island when an Ender Dragon dies.<br><br>
										Similarly to Nether Quartz, Ender Biotite drops an item, which can be used for crafting or to create Decorative Biotite Blocks, black quartz style blocks.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/cave-clay.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Underground Clay</div>
									<div class="feature-desc">
										Clay generates in patches underground, similarly to Dirt, albeit less often.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/crystal-caves.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Crystal Caves</div>
									<div class="feature-desc">
										Crystal Caves are rare underground scenery pieces. The caves will spawn with 2 (out of 8) colors of Cave Crystal block and a bunch of ores. The Cave Crystal blocks are a light source, and can be used to craft Colored Runes (read below), if that feature is enabled, plus they look really cool!
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/depth-mobs.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Depth Mobs</div>
									<div class="feature-desc">
										Ashen (left) and Dweller (right) mobs can spawn underground at low depth levels (below Y=20). These are both rare spawns compared to their normal counterparts.
										<br><br>
										The Ashen is a Skeleton variant that shoots Blindness arrows, which last for 2.5 seconds.
										<br><br>
										The Dweller is a Zombie variant that spawns invisible, but loses the invisibility after it attacks or is attacked. It also has a bit more armor.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/wraiths.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Wraiths and Curses</div>
									<div class="feature-desc">
										Wraiths are mobs that will spawn rarely in the nether, on Soul Sand. They spawn in packs and attack like a Zombie, albiet moving faster. When they strike their target, they inflict a short Slowness debuff and knock themselves back. Fighting them standing still is a bad idea. They also don't have their own sounds, instead appropriating other mobs' sounds.
										<br><br>
										Wraiths drop a Soul Bead item. Right clicking it will break it, and inflict a Curse debuff on the player for 20 minutes. Mobs in a 64 block radius of a player with Curse will spawn ignoring light levels and other conditions (like slime chunk restrictions). Curse can not be cleansed with Milk.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/pirates.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Pirates</div>
									<div class="feature-desc">
										Pirate ships will spawn in Ocean biomes. In the ships you'll find skeleton pirates, booty and hats. Yes, you can wear their hats, if you get them to drop.
										<br><br>
										Possible booty includes weapons, food, gold, TNT, Fire Charges, Pirate Hats and Sponges (rare).
										<br><br>
										Pirate mobs don't despawn.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/dank-dungeons.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Dungeon Variety</div>
									<div class="feature-desc">
										The Dungeon generator is suplemented by a random pick from Noodlor's Dank Dungeons, they still contain the same spawners and the same loot, but have new styles. The vanilla dungeons will still spawn.<br><br>

										You can find the entire original album <a href="https://imgur.com/a/JanPA">here</a> (emeralds replaced with cobblestone for Quark).
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/nether-blazes.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Blazes in the Nether</div>
									<div class="feature-desc">
										Blazes spawn naturally in the Nether. You won't get a lot of them, but they're definitely there to make the nether a bit more perilous.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/ocean-guardians.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Guardians in Oceans</div>
									<div class="feature-desc">
										Guardians spawn naturally in Oceans, rarely. See the previous feature.<br><br>
										If this feature is enabled, the Guardian sound is changed to not ignore distance for its volume (it does so in vanilla), so it's no longer a massive pain if you live near the ocean, or an ocean monument.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/swamp-shrooms.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Big Mushrooms in Swamps</div>
									<div class="feature-desc">
										Big mushrooms of both types will generate in Swamps, similarly to the Console Edition.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/ancient-tomes.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Ancient Tomes</div>
									<div class="feature-desc">
										Ancient Tomes are a rare loot item from dungeons and stronghold libraries. They come only in the highest level of an enchantment and can be combined with an Enchanted Book of the same enchantment to create one that's a level higher than the max. This costs 35 levels to create the book and an extra 35 to apply it.<br><br>
										The tomes can not be directly applied to an item. They also never contain enchantments that have no level or "Protection" type enchantments.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/colored-runes.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Colored Runes</div>
									<div class="feature-desc">
										Colored Runes are a rare loot item from dungeons, temples and nether fortresses. They can be applied to enchanted items in an anvil, and doing so changes the color of the enchantment glint from purple to the color of the rune. These come in all 16 colors of the spectrum.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/buried-treasure.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Buried Treasure</div>
									<div class="feature-desc">
										Treasure Maps can be found on Temples and Strongholds. These are special maps with an X on them. Travel to that X to uncover some buried treasure (a chest with Dungeon loot).
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/slime-bucket.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Slime in a Bucket</div>
									<div class="feature-desc">
										Right clicking a tiny slime with a bucket puts it in there. It remains quiet, but will start jumping inside the bucket if you find yourself in a slime chunk.<br><br>
										The slime can also be deployed back into the world by right clicking on a block. You can also use it as a replacement for slimeballs, you monster.
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/extra-arrows.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Extra Arrows</div>
									<div class="feature-desc">
										Three new arrows are added:
										<ul>
										<li>Ender Arrow: Crafted with an Arrow and an Ender Pearl, works like an ender pearl, but goes farther, since it's an arrow, yay.</li>
										<li>Explosive Arrow: Crafed with an Arrow and two pieces of Gunpowder, explodes where it hits.</li>
										<li>Torch Arrow: Crafted with an arrow and a Torch, places a torch where it hits. If it can't, it stays there so it can be picked up.</li>
										</ul>
										These can all be fired from dispensers, and will get used up even if the bow shooting them has Infinity.
										<br><br>
										<i>They're in the world module because I couldn't find anywhere else to put them. You can use them to explore, that's world-y enough right? :|</i>
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/realistic-world.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Realistic World Type</div>
									<div class="feature-desc">
										Alongside Amplified or Large Biomes, "Realistic" is a new world type that creates semi-realistic terrain. Expect rolling hills and very tall mountains. The cloud height is incresed to 260 in this world type.<br><br>
										In reality, this world type is just a wrapper for a Customized world preset, <a href="https://www.reddit.com/r/Minecraft/comments/4nfw3t/more_realistic_generation_preset_in_comment/">which you can find here</a>. Check it out for an album of what it looks like.
										<br><br>
										<i>If you want to use this in a server, set the world type to "quark_realistic".</i>
									</div>
								</div>
							</div>

							<div class="feature">
								<img class="feature-image" src="img/world/options.jpg"></img>
								<div class="feature-info">
									<div class="feature-title">Default World Options</div>
									<div class="feature-desc">
										All the options available for the Customized world type can be tweaked in the mod's configuration. These apply to the normal world type.<br><br>
										This is mostly a modpack maker feature, as all these options are already available through the Customized world type.
									</div>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>

		<button id="btt-button" class="mdl-button mdl-js-button mdl-button--fab mdl-button--colored" onclick="window.scrollTo(0, 0)">
			<i class="material-icons">arrow_upward</i>
		</button>

		<div id="footer">
			copyright lololol
		</div>

		<script>
			document.addEventListener('DOMContentLoaded', function() {
				document.getElementById('header').style['background-image'] = 'url(img/backgrounds/' + Math.floor(Math.random() * 8) + '.jpg)';
				document.getElementById('feature-counter').innerHTML = document.getElementsByClassName('feature').length;
			});
		</script>

		<script defer src="https://code.getmdl.io/1.2.1/material.min.js"></script>
	</body>
</html>