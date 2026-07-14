# friends-mod
👥 Friends Mod (NeoForge 1.21.1)
A psychological horror Minecraft mod that populates your singleplayer world with "friends" who aren't actually your friends.
This mod introduces creepy, player-like anomaly bots (anomaly006, anomaly008, anomaly009...) that stalk you from the shadows, show up in your Tab list, and disappear into thin air the moment they get too close.
👁️ Features
•	The "Friends" (Anomalies): Multiple variants of entities that look like players but are actually entities of pure dread.
•	Psychological Terror: The anomalies do 0.0 physical damage. Instead, they hunt you psychologically—stalking you from a distance, staring, and executing a terrifying jumpscare/screamer sequence when they get close.
•	Dynamic Tab List Spoofing: To trick you into thinking you aren't alone, these entities dynamically register themselves to the in-game Tab player list whenever they are near you. They will even show up with their custom "player" names!
•	Natural Spawning: Anomalies naturally spawn in the dark (light level 5 or lower). They will stalk you in deep caves, dark forests, or unlit corners of your base.
•	Ambient Dread: Uses static-corrupted Enderman audio cues, rare ambient whispers, and custom behavioral AI to keep you constantly on edge.
🛠️ Installation
	1.	Make sure you have NeoForge installed for Minecraft 1.21.1.
	2.	Download the latest compiled .jar from the Releases tab.
	3.	Drop the .jar file into your Minecraft mods folder:
•	Windows: %appdata%\.minecraft\mods
•	macOS: ~/Library/Application Support/minecraft/mods
	4.	Launch the game and try not to look behind you.
⚙️ Configuration
You can customize the mod's behavior by editing the configuration file located in /config/friendsmod-common.toml.
Option	Default Value	Description
TAB_LIST_ENABLED	true	Toggle whether anomalies show up on the Tab player list.
TAB_LIST_RANGE	48.0	The distance (in blocks) at which an anomaly starts appearing on your Tab list.
💻 Developer & Build Instructions
If you want to clone this repository and build the mod from source:
Prerequisites
•	Java 21 JDK
•	A terminal/command prompt
Build Command
Clone the repository and run the Gradle wrapper to build the .jar file:
git clone https://github.com/MisterInfinity-ua/friendsmod.git
cd friendsmod
./gradlew build

Once built successfully, your compiled mod file will be located in:
build/libs/friendsmod-1.0.0.jar
📜 License
This project is licensed under the MIT License. Feel free to use it, modify it, or include it in your custom modpacks!
