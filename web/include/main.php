<?php
	include 'include/common.php';
?>

<!DOCTYPE html>
<html>
<head>
	<title>quark</title>

    <meta charset="utf-8">
	<meta name="theme-color" content="#48ddbc">

	<meta name="twitter:card" content="summary_large_image" />
	<meta name="twitter:description" content="A Minecraft Java Edition Mod staying in the theme of Vanilla." />
	<meta name="twitter:title" content="quark" />
	<meta name="twitter:site" content="@VazkiiMods" />

	<link rel="stylesheet" href="style.css">
	<link rel="icon" href="favicon.ico">
</head>
<body>
	<div id="header">
		<div id="header-branding" class=data-entry-changer data-entry="home">
			<div id="header-icon"><img src="img/icon.png"></img></div>
			<div id="header-text">
				<div id="header-title"><span class="theme-color">q</span>uark</div>
				<div id="header-subtitle">Change your game, not your gameplay.</div>
			</div>
		</div>

		<div id="header-navbar">
			<div class="navbar-link data-entry-changer" data-entry="home">Home</div>
			<div class="navbar-link data-entry-changer" data-entry="features">Features</div>
			<div class="navbar-link data-entry-changer" data-entry="download">Download</div>
			<!--<div class="navbar-link data-entry-changer" data-entry="friends">Friends</div>-->
			<a href="old"><div class="navbar-link last">Old Site</div></a>
		</div>

		<hr>
	</div>

	<div id="main">
		<div class="content-holder active-holder" data-entry="home">
			<div class="info">
				You are looking at a beta test of the new website. It's not finished yet, and will look awful on mobile too. Expect things to maybe break, and content to be missing. (We're still filling in the Features secion, please excuse the mess)
			</div>

			<div id="big-branding">
				<div id="big-branding-background">
					<img src="img/features/management/boat-chests.jpg"></img>
					<img src="img/features/building/varied-chests.jpg"></img>
					<img src="img/features/automation/color-slime.jpg"></img>
					<img src="img/features/world/foxhounds.jpg"></img>
					<img src="img/features/automation/pistons-move-tes.jpg"></img>
					<img src="img/features/world/speleothems.jpg"></img>
					<img src="img/features/world/stonelings.jpg"></img>
					<img src="img/demo/underground-biomes.jpg"></img>
				</div>

				<div id="big-title"><span class="theme-color">q</span>uark</div>
				<div id="big-subtitle">is a Minecraft Mod that adds vanilla-like features</div>

				<div class="data-entry-changer std-button button-features" data-entry="features">
					<div class="button-title">Feature List</div>
					<div class="button-subtitle"><?php include 'include/feature_count.php'; ?> and counting!</div>
				</div>

				<div class="data-entry-changer std-button button-download" data-entry="download">
					<div class="button-title">Download Quark</div>
					<div class="button-subtitle">for Minecraft Java Edition</div>
				</div>
			</div>

			<div id="social-strip">
				<div class="social-link"><img src="https://twitter.com/favicon.ico"></img> <a href="https://twitter.com/VazkiiMods">Twitter</a></div>
				<div class="social-link"><img src="https://github.com/favicon.ico"></img> <a href="https://github.com/Vazkii/Quark">GitHub</a></div>
				<div class="social-link"><img src="https://discordapp.com/assets/07dca80a102d4149e9736d4b162cff6f.ico"></img> <a href="https://vazkii.net/discord">Discord</a></div>
				<div class="social-link"><img src="https://c5.patreon.com/external/favicon/favicon-16x16.png"></img> <a href="https://patreon.com/Vazkii">Patreon</a></div>
				<div class="social-link"><img src="https://vazkii.net/favicon.ico"></img> <a href="https://vazkii.net">vazkii.net</a></div>
			</div>

			<hr>
			
			<div class="main-page-section">
				<div class="section-header">About</div>
				<p>
					Quark is a mod for Minecraft Java Edition. It aims to create an experience akin to the "vanilla" experience, by having a very simple motto: <i>anything that would be added to Quark could also be added to the default game without compromising its gameplay style.</i> A quark is a very small thing, and the name of the mod derives from that, as each individual feature is small, adding up to a large whole.
				</p>
				<p>
					Every feature in quark can be disabled and tweaked individually. When you load up the game, you'll see a <span class="theme-color">q</span> button in your main menu. Clicking this button lets you configure the mod. You can tweak everything your way, or just disable a few things you don't like, up to you!
				</p>
			</div>
			<hr>

			<div class="main-page-section">
				<div class="section-header">The Team</div>

				<div class="team-holder">
					<div class="team-member">
						<div class="team-avatar"><img src="img/avatars/vazkii.png"></img></div>
						<div class="team-name"><a href="https://twitter.com/vazkii">Vazkii</a></div>
						<div class="team-role">Lead Developer / Designer</div>
					</div>
					<div class="team-separator"></div>

					<div class="team-member">
						<div class="team-avatar"><img src="img/avatars/wiresegal.png"></img></div>
						<div class="team-name"><a href="https://twitter.com/wiresegal">Wire Segal</a></div>
						<div class="team-role">Developer / Maintainer</div>
					</div>
					<div class="team-separator"></div>

					<div class="team-member">
						<div class="team-avatar"><img src="img/avatars/mcvinnyq.png"></img></div>
						<div class="team-name"><a href="https://twitter.com/mcvinnyq">MCVinnyq</a></div>
						<div class="team-role">Lead Artist</div>
					</div>
				</div>
			</div>
			<hr>

			<div class="main-page-section">
				<div class="section-header">Feature Spotlight</div>

				<div class="spotlight-entry spotlight-left">
					<div class="spotlight-image"><img src="img/demo/underground-biomes.jpg"></img></div>
					<div class="spotlight-info">
						<div class="spotlight-header">Upgraded Caves</div>
						<div class="spotlight-desc">Quark adds all sorts of new features to caves - underground biomes, stalactites and stalagmites, new mobs, a pickaxe boomerang, you name it!</div>
					</div>
				</div>

				<div class="spotlight-entry spotlight-right">
					<div class="spotlight-info">
						<div class="spotlight-header">New Redstone Opportunities</div>
						<div class="spotlight-desc">Power up your redstone game with many new redstone features, such as pistons moving tile entities, colored slime blocks, block placing, and much more.</div>
					</div>
					<div class="spotlight-image"><img src="img/features/automation/color-slime.jpg"></img></div>
				</div>

				<div class="spotlight-entry spotlight-left">
					<div class="spotlight-image"><img src="img/features/building/thatch.jpg"></img></div>
						<div class="spotlight-info">
						<div class="spotlight-header">Building Blocks</div>
						<div class="spotlight-desc">Arm your palette with a large assortment of building blocks, from Thatch (pictured), to Stained Wood Planks, Magma Bricks, and many others!</div>
					</div>
				</div>

				<div class="spotlight-entry spotlight-right">
					<div class="spotlight-info">
						<div class="spotlight-header">Vertical Slabs</div>
						<div class="spotlight-desc">Yeah, we got 'em.</div>
					</div>
					<div class="spotlight-image"><img src="img/features/building/vertical-slabs.jpg"></img></div>
				</div>

				<div class="data-entry-changer std-button button-long button-next" data-entry="features">
					<div class="button-title">Full Feature List</div>
				</div>
			</div>
		</div>

		<div class="content-holder" data-entry="features">
			<div class="section-header">Features</div>
			<div class="info">
				This page shows the list of features available for Quark in Minecraft 1.14.4 and further versions. If you're looking for 1.12.2 or previous, please check the old site in the header.
			</div>

			<div id="feature-category-strip">
				<div class="navbar-link category-navbar-link data-category-changer" data-category="automation">Automation</div>
				<div class="navbar-link category-navbar-link data-category-changer" data-category="building">Building</div>
				<div class="navbar-link category-navbar-link data-category-changer" data-category="client">Client</div>
				<div class="navbar-link category-navbar-link data-category-changer" data-category="management">Management</div>
				<div class="navbar-link category-navbar-link data-category-changer" data-category="tools">Tools</div>
				<div class="navbar-link category-navbar-link data-category-changer" data-category="tweaks">Tweaks</div>
				<div class="navbar-link category-navbar-link data-category-changer" data-category="vanity">Vanity</div>
				<div class="navbar-link category-navbar-link data-category-changer last" data-category="world">World</div>
			</div>
			
			<?php include 'features.php'; ?>
		</div>

		<div class="content-holder" data-entry="download">
			<div class="section-header">Download</div>

			<div class="warning">
				The only website that hosts <i>official</i> versions of Quark is <b>CurseForge</b>. Make sure you get it from there only. Other websites such as <b>9minecraft</b> or <b>mc-mods</b> are distributing old or tampered versions that may contain bugs or malware.
			</div>

			<div class="info">
				Access to the Quark 1.14.4 beta is currently restricted to specific people. You can access it if you are one or more of...
				<ul>
					<li>A 5$ or higher patron on <a href="https://www.patreon.com/Vazkii">Vazkii's Patreon</a></li>
					<li>A subscriber to <a href="https://www.twitch.tv/Vazkii">Vazkii's Twitch Channel</a> (Prime counts!)</li>
					<li>Specially selected (aka, have the "Nerd" role on the Discord Server)</li>
				</ul>
				If you have the appropriate roles, you can access the beta files in the <i>#betas</i> channel of <a href="https://vazkii.net/discord">the Discord server</a>.
				<ul>
					<li>As usual, AutoRegLib is required. The latest ARL version will be pinned in <i>#betas</i>.</li>
					<li>If you're a patron and don't yet have the appropriate roles, please send a DM over patreon with your info so they can be sorted out. For twitch subs, make sure to link your twitch to discord and it's done automatically.</li>
				</ul>
			</div>

			<div class="download-holder">
				<div class="download-strip">
					<a href="https://www.curseforge.com/minecraft/mc-mods/quark" class="no-external"><div class="std-button button-download">
						<div class="button-title">Download Quark</div>
						<div class="button-subtitle">from CurseForge</div>
					</div></a>

					<a href="https://www.curseforge.com/minecraft/mc-mods/autoreglib" class="no-external"><div class="std-button button-download-muted">
						<div class="button-title">Download AutoRegLib</div>
						<div class="button-subtitle">(needed for Quark to work)</div>
					</div></a>
				</div>
				<div class="std-button button-long" id="install-instructions-button">
					<div class="button-title">Show How to Install</div>
				</div>
				<div id="install-instructions">
					<div class="section-header">How to Install</div>
						<p>
							If you're on Windows or Mac, you can use the <a href="https://app.twitch.tv/">Twitch app</a> to install the mod:
							<ol>
								<li>Install Twitch App and log in.</li>
								<li>Select "Mods" in the top bar, and then Minecraft.</li>
								<li>Click Create Custom Profile, name it and pick your Minecraft version.</li>
								<li>Open the profile, and click Get More Content</li>
								<li>Search for Quark and install it. This will also install AutoRegLib.</li>
								<li>Go back to the profile and hit Play. You're done.</li>
							</ol>
						</p>
						<hr>
						<p>
							If you can't or don't want to use the Twitch app, you can install the mod manually:
							<ol>
								<li>Locate the version of Minecraft Forge for your target Minecraft version <a href="https://files.minecraftforge.net/">here</a>. Get the <i>latest</i> one.</li>
								<li>Download the Installer and run it, click OK. This will do a bunch of stuff so wait a bit until it's done.</li>
								<li>Load up your Minecraft Java Edition Launcher, and run the "forge" Installation it created for you.</li>
								<li>Click the new "Mods" button in your title screen, and then "Open Mods Folder".</li>
								<li>Download both Quark and AutoRegLib using the download buttons above.</li>
									<ul>
										<li>The Download link on the CurseForge website is in the top right corner.</li>
									</ul>
								<li>Drag the two files you just downloaded into the mods folder.</li>
								<li>Exit the game and open it again. You're done.</li>
							</ol>
						</p>
						<hr>
						<p>
							If you want to install the mod on a server:
							<ol>
								<li>Run your forge installer like before, but select "Install server" this time.</li>
								<li>Click the ... button and pick a folder to put your server. It has to be empty so you should make a new one.</li>
								<li>Open the folder where you put your server, and make a new folder, named "mods"</li>
								<li>Download AutoRegLib and Quark, and put the downloaded files into that folder.</li>
								<li>Run the server using the forge jar, <i>not the minecraft_server jar</i>. Don't forget to accept the EULA.</li>
							</ol>
						</p>
				</div>
			</div>

			<hr>

			<div class="section-header">Need Help?</div>
			<ul>
				<li>If you found a bug, you can report it on the <a href="https://github.com/Vazkii/Quark/issues">Issue Tracker</a>.</li>
				<li>If you need someone to help you solve a problem, try our <a href="https://vazkii.net/discord">Discord Server</a>.</li>
			</ul><br>
		</div>

		<div class="content-holder" data-entry="friends">
			wip
		</div>
	</div>


	<div id="footer">
		<hr>

		<div id="footer-info">	
			website by vazkii (<a href="mailto:vazkii@hotmail.com" class="no-external">contact me</a>)<br>
			copyright lolololol
		</div>
		
		<div id="footer-sponsor">
			<a href="https://www.creeperhost.net/" class="no-external"><img src="https://vazkii.net/sellout.png"></img></a>
		</div>
	</div>

	<div id="btt-holder">
		<div id="button-btt", class="std-button">
			<div class="button-title">&#x1f879;</div>
		</div>
	</div>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="quark.js"></script>
</body>
</html>