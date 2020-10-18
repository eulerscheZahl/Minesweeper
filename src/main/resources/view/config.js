import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { MyToggle } from './MyToggle.js';

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TooltipModule,
	MyToggle
];

export const options = [
  MyToggle.defineToggle({
    // The name of the toggle
    // replace "myToggle" by the name of the toggle you want to use
    toggle: 'd',
    // The text displayed over the toggle
    title: 'THEME',
    // The labels for the on/off states of your toggle
    values: {
      'MAXIMIZED': true,
      'RETRO': false
    },
    // Default value of your toggle
    default: false
  }),
]