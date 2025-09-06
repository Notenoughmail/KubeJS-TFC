
TFC.misc.register2DNoiseForInspection('flat', (x, z) => 0);
TFC.misc.register2DNoiseForInspection('base', TFC.misc.newOpenSimplex2D(6249785832124));
TFC.misc.register2DNoiseForInspection('cell', TFC.misc.cellular2D(4861493235646));
const cell = TFC.misc.cellular2D(977615453);
TFC.misc.register2DNoiseForInspection('cell_then', cell.then(cell => cell.f1() * cell.f2()));
TFC.misc.register2DNoiseForInspection('cell_x', cell.then(cell => cell.x()));
TFC.misc.register2DNoiseForInspection('cell_y', cell.then(cell => cell.y()));
const transposeAndRotateTest2D = TFC.misc.customNoise2D((x, z) => {
    if (Math.round(x) % 2 == 0) {
        return 1;
    } else {
        return -1;
    }
});
TFC.misc.register2DNoiseForInspection('fence', transposeAndRotateTest2D);
TFC.misc.register2DNoiseForInspection('transpose', transposeAndRotateTest2D.transpose());
TFC.misc.register2DNoiseForInspection('rotate_15', transposeAndRotateTest2D.rotate(15));
TFC.misc.register2DNoiseForInspection('rotate_30', transposeAndRotateTest2D.rotate(30));
TFC.misc.register2DNoiseForInspection('rotate_45', transposeAndRotateTest2D.rotate(45));
TFC.misc.register2DNoiseForInspection('rotate_60', transposeAndRotateTest2D.rotate(60));
TFC.misc.register2DNoiseForInspection('rotate_75', transposeAndRotateTest2D.rotate(75));
TFC.misc.register2DNoiseForInspection('rotate_80', transposeAndRotateTest2D.rotate(90));
TFC.misc.register2DNoiseForInspection('rotate_132', transposeAndRotateTest2D.rotate(132));

TFC.misc.register3DNoiseForInspection('flat', (x, y, z) => 0);
TFC.misc.register3DNoiseForInspection('base', TFC.misc.newOpenSimplex3D(97621631463));
TFC.misc.register3DNoiseForInspection('cell', TFC.misc.cellular3D(79431531351));
TFC.misc.register3DNoiseForInspection('3d with space', (x, y, z) => KMath.v3d(x, y, z).length());
const sinY = TFC.misc.customNoise2D((y, _) => Math.sin(y * 2 * KMath.PI));
const transposeAndRotateTest3D = TFC.misc.customNoise3D((x, y, z) => {
    if (y < sinY.noise(y, 0)) {
        return Math.cos(x) * Math.sin(z);
    } else {
        return 100000000;
    }
});
TFC.misc.register3DNoiseForInspection('fence', transposeAndRotateTest3D);
TFC.misc.register3DNoiseForInspection('transpose_xz', transposeAndRotateTest3D.transposeXZ());
TFC.misc.register3DNoiseForInspection('transpose_xy', transposeAndRotateTest3D.transposeXZ());
TFC.misc.register3DNoiseForInspection('transpose_yz', transposeAndRotateTest3D.transposeYZ());
TFC.misc.register2DNoiseForInspection('dissolve', transposeAndRotateTest3D.dissolve(transposeAndRotateTest2D));
TFC.misc.register3DNoiseForInspection('rotate_x', transposeAndRotateTest3D.rotateX(45));
TFC.misc.register3DNoiseForInspection('rotate_y', transposeAndRotateTest3D.rotateY(45));
TFC.misc.register3DNoiseForInspection('rotate_z', transposeAndRotateTest3D.rotateZ(45));

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

const rockType = TFC.misc.cellular2D(0)
    .spread(0.0076)
    .scaled(0, 4)
    .map(v => Math.round(v));
const rockVal = TFC.misc.cellular2D(0)
    .spread(0.0035)
    .scaled(-0x40000000, 0x40000000)
    .rotate(30)
    .map(v => Math.round(v) << 2);

TFC.misc.register2DNoiseForInspection(
    'rock_type',
    rockType
);
TFC.misc.register2DNoiseForInspection(
    'rock_val',
    rockVal
);
TFC.misc.register2DNoiseForInspection(
    'rock_noise',
    rockType.add(rockVal)
);
TFC.misc.register2DNoiseForInspection(
    'forest_noise',
    TFC.misc.newOpenSimplex2D(0)
        .scaled(0, 4)
        .spread(0.005)
        .map(v => Math.round(v))
);
TFC.misc.register2DNoiseForInspection('infinity', TFC.misc.customNoise2D((x, z) => 1 / 0));
TFC.misc.register2DNoiseForInspection('-infinity', TFC.misc.customNoise2D((x, z) => -1 / 0));

const layeredArea = TFC.misc.uniformLayeredArea(413567326);

for (let i = 0 ; i < 3 ; i++) {
    layeredArea.zoom(false, 19763144126).smooth(79784123632);
}

for (let i = 0 ; i < 6 ; i++) {
    layeredArea.zoom(false, 451364589723);
}

layeredArea.smooth(71214856214)
    .zoom(false, 854126548632)
    .smooth(145256147896)

TFC.misc.register2DNoiseForInspection('rockLayerArea', (x, z) => layeredArea.getAt(x, z));
TFC.misc.register2DNoiseForInspection('rockLayerAreaType', (x, z) => layeredArea.getAt(x, z) & 0b11);
