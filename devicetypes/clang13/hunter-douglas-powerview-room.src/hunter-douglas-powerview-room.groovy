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
	definition (name: "Hunter Douglas PowerView Room", namespace: "clang13", author: "Chris Lang") {
		capability "Actuator"
		capability "Window Shade"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
        standardTile("main", "command.refresh", width:2, height:2, decoration: "flat") {
            state "default", label:'testing', action:"presetPosition"
        }
        standardTile("open", "command.refresh", width:2, height:2, decoration: "flat") {
            state "default", label:'OPEN', action:"open"
        }
        standardTile("close", "command.refresh", width:2, height:2, decoration: "flat") {
            state "default", label:'CLOSE', action:"close"
        }
        
        main(["main"])
        details(["open", "close"])
    }
}

def initialize() {
}

// handle commands
def open() {
	log.debug "Executing 'open'"
	parent.openRoom(device)    
}

def close() {
	log.debug "Executing 'close'"
	parent.closeRoom(device)    
}
