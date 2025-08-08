
TFC.misc.register2DNoiseForInspection('flat', (x, z) => 0);
TFC.misc.register2DNoiseForInspection('base', TFC.misc.newOpenSimplex2D(6249785832124));
TFC.misc.register2DNoiseForInspection('cell', TFC.misc.cellular2D(4861493235646));

TFC.misc.register3DNoiseForInspection('flat', (x, y, z) => 0);
TFC.misc.register3DNoiseForInspection('base', TFC.misc.newOpenSimplex3D(97621631463));
TFC.misc.register3DNoiseForInspection('cell', TFC.misc.cellular3D(79431531351));
TFC.misc.register3DNoiseForInspection('3d with space', (x, y, z) => Math.sin(x) * Math.cos(z) * Math.sin(y))

var fnl = TFC.misc.fnl(79461231636);
fnl.SetNoiseType('Cellular');
fnl.SetCellularReturnType('CellValue');
fnl.SetCellularDistanceFunction('Hybrid');
fnl.SetCellularJitter(0.87);
fnl.SetFractalType('FBm');
fnl.SetFractalOctaves(2);
fnl.SetFrequency(0.04);
TFC.misc.register2DNoiseForInspection('fnl', TFC.misc.fnl2Noise2D(fnl));
TFC.misc.register3DNoiseForInspection('fnl', TFC.misc.fnl2Noise3D(fnl));
