import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

// import type { IConfigTef } from './IIdeployTef';

export interface Spec extends TurboModule {
  onInitTef(): void;
  configTef(settings: any): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('IdeployTef');
