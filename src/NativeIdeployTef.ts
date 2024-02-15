import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  onInitTef(): void;
  configTef(
    name: string,
    vesion: string,
    pinpad: string,
    pinPadText: string,
    doc: string
  ): void;
  payDeb(valeu: string): void;
  payCred(valeu: string, type: string, installments: string): void;
  payPix(valeu: string): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('IdeployTef');
