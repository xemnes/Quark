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
	<meta name="twitter:description" content="A Minecraft mod built with Vanilla's themes." />
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
				<div id="header-subtitle">Change your game. Not your gameplay.</div>
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
				<div id="big-subtitle">is a Minecraft mod that aims to enhance Vanilla</div>

				<div class="data-entry-changer std-button button-features" data-entry="features">
					<div class="button-title">Feature List</div>
					<div class="button-subtitle"><?php include 'include/feature_count.php'; ?> and counting!</div>
				</div>

				<div class="data-entry-changer std-button button-download" data-entry="download">
					<div class="button-title">Download</div>
					<div class="button-subtitle">for Minecraft Java Edition 1.14.4</div>
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
					Quark is a mod for Minecraft Java Edition, aiming to enhance the base game, using a very simple motto: <i>Anything added to Quark could also be added to the default game without compromising its gameplay style.</i> The name of the mod derives from this focus on small, simple change: Like quarks, each individual feature is small, but they build into a larger whole.
				</p>
				<p>
					Every feature in Quark can be disabled and tweaked individually. When you load up the game, you'll see a <span class="theme-color">q</span> button in your main menu. Clicking this button lets you configure the mod. You can tweak everything just your way, or even disable things you don't like!
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
						<div class="spotlight-desc">Power up your redstone game with many new redstone features! Pistons can move tile entities, a randomizer diode, dispenser block placing, and much, much more.</div>
					</div>
					<div class="spotlight-image"><img src="img/features/automation/redstone-circuits.jpg"></img></div>
				</div>

				<div class="spotlight-entry spotlight-left">
					<div class="spotlight-image"><img src="img/features/building/thatch.jpg"></img></div>
					<div class="spotlight-info">
						<div class="spotlight-header">Building Blocks</div>
						<div class="spotlight-desc">Fuel your inner artist with a large assortment of new building blocks, from Thatch (pictured), to Stained Wood Planks, Magma Bricks, and many others!</div>
					</div>
				</div>

				<div class="spotlight-entry spotlight-right">
					<div class="spotlight-info">
						<div class="spotlight-header">Vertical Slabs</div>
						<div class="spotlight-desc">Yeah. We got 'em.</div>
					</div>
					<div class="spotlight-image"><img src="img/features/building/vertical-slabs.jpg"></img></div>
				</div>

				<div class="data-entry-changer std-button button-next" data-entry="features">
					<div class="button-title">Full Feature List</div>
				</div>
			</div>
		</div>

		<div class="content-holder" data-entry="features">
			<div class="section-header">Features</div>
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
			<hr>
			<?php include 'features.php'; ?>
		</div>

		<div class="content-holder" data-entry="download">
			<div class="section-header">Download</div>
			<p>
				Access to the Quark 1.14.4 beta is currently restricted to specific people. You can access it if you are:
				<ul>
					<li>Pledging 5$ or higher to <a href="https://www.patreon.com/Vazkii">Vazkii's Patreon</a></li>
					<li>A subscriber to <a href="https://www.twitch.tv/Vazkii">Vazkii's Twitch Channel</a> (Prime counts!)</li>
					<li>Selected by Vazkii (That's what the "Nerd" role on the Discord Server represents!)</li>
				</ul>
				If you have the appropriate role, you can access the beta files in the <i>#betas</i> channel of <a href="https://vazkii.net/discord">the Discord server</a>.
				<ul>
					<li>As normal, AutoRegLib is required. The latest ARL version will be provided, pinned, in <i>#betas</i>.</li>
					<li>If you're a patron and don't yet have the appropriate roles, please send a DM over patreon with your info so it can be sorted out.</li>
				</ul>
			</p>
		</div>

		<div class="content-holder" data-entry="friends">
			wip
		</div>
	</div>


	<div id="footer">
		<hr>
		<div id="footer-info">	
			website by vazkii (<a href="mailto:vazkii@hotmail.com">contact me</a>)<br>
			copyright lolololol
		</div>
		
		<div id="footer-sponsor">
			<a href="https://www.creeperhost.net/"><img src="https://vazkii.net/sellout.png"></img></a>
		</div>
	</div>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="quark.js"></script>
</body>
</html>
