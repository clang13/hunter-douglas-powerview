/**
 *  Hunter Douglas PowerView Shade
 *
 *  Copyright 2017 Chris Lang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Hunter Douglas PowerView Shade", namespace: "clang13", author: "Chris Lang") {
		capability "Actuator"
        capability "Battery"
        capability "Refresh"
		capability "Sensor"
        capability "Switch Level"
		capability "Window Shade"
        
        command "calibrate"
        command "jog"
        command "setBottomPosition", ["number"]
        command "setTopPosition", ["number"]
        
        attribute "bottomPosition", "number"
        attribute "topPosition", "number"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
        standardTile("main", "device.switch") {
            state("unknown", label:'unknown', action:"refresh.refresh", icon:"st.Home.home9-icn", backgroundColor:"#ffa81e")
            state("closed",  label:'down', action:"open", icon:"st.Home.home9-icn", backgroundColor:"#bbbbdd", nextState: "opening")
            state("open",    label:'up', action:"close", icon:"st.Home.home9-icn", backgroundColor:"#ffcc33", nextState: "closing")
            state("partially open", label:'partially open',/* TODO? */ action:"st.Home.home9-icn", icon:"st.Transportation.transportation13", backgroundColor:"#ffcc33")
            state("closing", label:'closing', action:"presetPosition", icon:"st.Home.home9-icn", backgroundColor:"#bbbbdd")
            state("opening", label:'closing', action:"presetPosition", icon:"st.Home.home9-icn", backgroundColor:"#ffcc33")
            state("default", label:'preset', action:"presetPosition", icon:"st.Home.home9-icn", backgroundColor:"#ffcc33")
        }
        
        standardTile("refresh", "command.refresh", width:2, height:2, decoration: "flat") {
            state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
        }
        standardTile("jog", "command.refresh", width:2, height:2, decoration: "flat") {
            state "default", label:'JOG', action:"jog"
        }
        standardTile("calibrate", "command.refresh", width:2, height:2, decoration: "flat") {
            state "default", label:'CALIBRATE', action:"calibrate"
        }

        valueTile("topPosition", "device.topPosition", height: 1, width: 2, decoration: "flat") {
            state "val", label: 'Top ${currentValue}'
        }
        controlTile("topPositionSlider", "device.topPosition", "slider", height: 1, width: 4, range: "(0..100)") {
            state("default", action:"setTopPosition")
        }

        valueTile("bottomPosition", "device.bottomPosition", height: 1, width: 2, decoration: "flat") {
            state "val", label: 'Bottom ${currentValue}'
        }
        controlTile("bottomPositionSlider", "device.bottomPosition", "slider", height: 1, width: 4, range: "(0..100)") {
            state("default", action:"setBottomPosition")
        }
        
        
		valueTile("battery", "device.battery", decoration: "flat", width: 2, height: 2){
			state "battery", label:'${currentValue}% \nBattery', unit:""
		}
        
        main(["main"])
        details([/* "shade", "on", "off", */ "topPosition", "topPositionSlider", "bottomPosition", "bottomPositionSlider", "battery", "refresh", "jog", "calibrate"])
    }
}

def initialize() {
	pollAndMaybeUpdateBattery(true)
}

def refresh() {
	parent?.pollShade(device, updateBattery)
}
    
public handleEvent(shadeJson) {
	log.debug "handleEvent: shadeJson = ${shadeJson}"
    if (shadeJson?.positions) {
    	def positions = shadeJson.positions
    	if (positions.posKind1) {
        	updatePosition(positions.position1, positions.posKind1)
        }
    	if (positions.posKind2) {
        	updatePosition(positions.position2, positions.posKind2)
        }
	} else {
    	// If a result doesn't include position, sometimes reissuing the poll will return it.
	    def now = now()
        if (!state?.lastPollRetry || (state?.lastPollRetry - now) > (60 * 10 * 1000)) {
        	log.debug "event didn't contain position, retrying poll"
        	state?.lastPollRetry = now
            refresh()
        }
    }
    
    if (shadeJson?.batteryStrength) {
    	// Is this right? Guessing based on observed range.
        def batteryPercent = (int) (shadeJson.batteryStrength * 100 / 255)
        sendEvent(name: "battery", value: batteryPercent)
    }
}

def updatePosition(position, posKind) {
	def intPosition = (int) (position * 100 / 65535)
    def eventName = (posKind == 1) ? "bottomPosition" : "topPosition"
    log.debug "sending event ${eventName} with value ${intPosition}"
    
    sendEvent(name: eventName, value: intPosition)
}

// parse events into attributes
def parse(String description) {
}

// handle commands
def open() {
	log.debug "Executing 'open'"
    // TODO verify different types of shades (bottom-up, top-down, top-down/bottom-up)
	parent.setPosition(device, [bottomPosition: 100, topPosition: 0])
}

def close() {
	log.debug "Executing 'close'"
    // TODO verify different types of shades (bottom-up, top-down, top-down/bottom-up)
	parent.setPosition(device, [bottomPosition: 0, topPosition: 0])
}

def presetPosition() {
}

def calibrate() {
	parent.calibrateShade(device)
}
    
def jog() {
	parent.jogShade(device)
}

def setBottomPosition(bottomPosition) {
	bottomPosition = Math.min(Math.max(bottomPosition, 0), 100)
	parent.setPosition(device, [bottomPosition: bottomPosition])
}

def setTopPosition(topPosition) {
	topPosition = Math.min(Math.max(topPosition, 0), 100)
	parent.setPosition(device, [topPosition: topPosition])
}
