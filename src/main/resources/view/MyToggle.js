import {
    api as entityModule
} from './entity-module/GraphicEntityModule.js'
import {
    ErrorLog
} from './core/ErrorLog.js'
import {
    MissingToggleError
} from './toggle-module/errors/MissingToggleError.js'
import {
    DuplicateToggleValueError
} from './toggle-module/errors/DuplicateToggleValueError.js'
import {
    EntityFactory
} from './entity-module/EntityFactory.js'
import {
    Entity
} from './entity-module/Entity.js'

export class MyToggle {
    static getTime() {
        var date = new Date()
        // https://stackoverflow.com/a/8888498
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0' + minutes : minutes;
        var strTime = hours + ':' + minutes + ' ' + ampm;
        return strTime;
    }

    constructor(assets) {
        this.text = EntityFactory.create("T")
        this.text.id = 10000
        entityModule.entities.set(this.text.id, this.text)

        this.text.states[0] = [Entity.createState(0, {
            ...this.text.defaultState,
            fontSize: 35,
            fontFamily: "MS Sans",
            x: 1770,
            y: 1028,
            visible: true,
            text: MyToggle.getTime(),
            t: 0
        }, {})]

        setInterval(function() {
            entityModule.container.children[2].children[0].text = MyToggle.getTime()
        }, 1000)

        this.previousFrame = {}
        this.missingToggles = {}

        MyToggle.refreshContent = () => {
            if (entityModule.container.children.length < 2) return
            if (MyToggle.toggles.d) {
                entityModule.container.children[0].visible = false
                entityModule.container.children[2].visible = false
                entityModule.container.children[1].x = 48
                entityModule.container.children[1].y = 53
                entityModule.container.children[1].transform.scale.x = 3.8
                entityModule.container.children[1].transform.scale.y = 3.8
            } else {
                entityModule.container.children[0].visible = true
                entityModule.container.children[2].visible = true
                entityModule.container.children[1].x = 667
                entityModule.container.children[1].y = 307
                entityModule.container.children[1].transform.scale.x = 2.5
                entityModule.container.children[1].transform.scale.y = 2.5
            }
        }
    }

    registerToggle(entity, name, state) {
        this.previousFrame.registered[entity.id] = {
            "name": name,
            "state": state
        }
    }

    static refreshContent() {}

    static defineToggle(option) {
        MyToggle.toggles[option.toggle] = option.default
        option.get = () => MyToggle.toggles[option.toggle]
        option.set = (value) => {
            MyToggle.toggles[option.toggle] = value
            MyToggle.refreshContent()
        }
        return option
    }

    static get name() {
        return 'toggles'
    }

    updateScene(previousData, currentData, progress) {
        this.currentFrame = currentData
        this.currentProgress = progress
        MyToggle.refreshContent()
    }

    handleFrameData(frameInfo, data) {
    }

    reinitScene(container, canvasData) {
        MyToggle.refreshContent()
    }
}

MyToggle.toggles = {}
MyToggle.optionValues = {}