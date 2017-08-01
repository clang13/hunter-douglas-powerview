# hunter-douglas-powerview
SmartThings SmartApp and DTH for controlling Hunter Douglas blinds via PowerView hub

## Installation
More detailed instructions to come. For now, this assumes you are familiar with SmartThings GitHub Integration
1. Add repo owner 'clang13' repo 'hunter-douglas-powerview' to your GitHub Settings
2. In your My SmartApps tab, 'Update from Repo', choose the SmartApp, check Publish, Execute Update
3. In your My Device Handles tab, 'Update from Repo', choose the device handles, check Publish, Execute Update
4. In your SmartThings App, go to Marketplace | SmartApps tab, scroll to My Apps, choose 'Hunter Douglas PowerView'
5. Enter your PowerView Hub's IP address (you can find this in the PowerView app by clicking the upper-left menu, selecting your Hub, Info, Network Info)
6. Click to discover and configure shades

## Shades
Shade support is basic, but functional. You can:
- Create Things for individual shades, or for all shades discovered on the hub
- Set the top and bottom positions (work in progress, not all shade types are fully supported)
- Issue Jog and Calibrate commands
- Refresh status

The accuracy of the data returned from the PowerView Hub is quite variable, so shade position and battery info may be out of date.

## Scenes
You can:
- Create Things for individual scenes, or for all scenes discovered on the hub
- Trigger scenes either as a momentary switch or as a window covering via the 'presetPosition' command

## Rooms
Room support is tailored to work with EchoSistant. EchoSistant allows specifying window coverings to be opened or closed as a group. Rather than
specifying multiple sets of shades and having EchoSistant trigger them individually, you can assign an Open and/or Close Scene to a Room,
and assign the Room in EchoSistant -- "opening" or "closing" the Room will in turn trigger the corresponding scene.

Works in progress:
- Discovery/configuration needs to be more reliable
- If possible, status querying needs to be more reliable, but this may be a limitation of the PowerView Hub
- More configurable refresh time
- Support for multi-room scenes
- (Possibly) optionally support LAN-based service intermediary to query PowerView and only report updates via callback, to reduce load
on SmartThings.

