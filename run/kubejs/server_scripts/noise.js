
TFC.noise.inspect2D('flat', (x, z) => 0);
TFC.noise.inspect2D('base', TFC.noise.openSimplex2D(6249785832124));
const transposeAndRotateTest2D = TFC.noise.customNoise2D((x, z) => {
    if (Math.round(x) % 2 == 0) {
        return 1;
    } else {
        return -1;
    }
});
TFC.noise.inspect2D('fence', transposeAndRotateTest2D);
TFC.noise.inspect2D('transpose', transposeAndRotateTest2D.transpose());
TFC.noise.inspect2D('rotate_15', transposeAndRotateTest2D.rotate(15));
TFC.noise.inspect2D('rotate_30', transposeAndRotateTest2D.rotate(30));
TFC.noise.inspect2D('rotate_45', transposeAndRotateTest2D.rotate(45));
TFC.noise.inspect2D('rotate_60', transposeAndRotateTest2D.rotate(60));
TFC.noise.inspect2D('rotate_75', transposeAndRotateTest2D.rotate(75));
TFC.noise.inspect2D('rotate_80', transposeAndRotateTest2D.rotate(90));
TFC.noise.inspect2D('rotate_132', transposeAndRotateTest2D.rotate(132));

TFC.noise.inspect3D('flat', (x, y, z) => 0);
TFC.noise.inspect3D('base', TFC.noise.openSimplex3D(97621631463));
TFC.noise.inspect3D('3d with space', (x, y, z) => KMath.v3d(x, y, z).length());
const sinY = TFC.noise.customNoise2D((y, _) => Math.sin(y * 2 * KMath.PI));
const transposeAndRotateTest3D = TFC.noise.customNoise3D((x, y, z) => {
    if (y < sinY.noise(y, 0)) {
        return Math.cos(x) * Math.sin(z);
    } else {
        return 100000000;
    }
});
TFC.noise.inspect3D('fence', transposeAndRotateTest3D);
TFC.noise.inspect3D('transpose_xz', transposeAndRotateTest3D.transposeXZ());
TFC.noise.inspect3D('transpose_xy', transposeAndRotateTest3D.transposeXZ());
TFC.noise.inspect3D('transpose_yz', transposeAndRotateTest3D.transposeYZ());
TFC.noise.inspect2D('dissolve', transposeAndRotateTest3D.dissolve(transposeAndRotateTest2D));
TFC.noise.inspect3D('rotate_x', transposeAndRotateTest3D.rotateX(45));
TFC.noise.inspect3D('rotate_y', transposeAndRotateTest3D.rotateY(45));
TFC.noise.inspect3D('rotate_z', transposeAndRotateTest3D.rotateZ(45));

var fnl = TFC.noise.fastNoiseLite(79461231636);
fnl.SetNoiseType('Cellular');
fnl.SetCellularReturnType('CellValue');
fnl.SetCellularDistanceFunction('Hybrid');
fnl.SetCellularJitter(0.87);
fnl.SetFractalType('FBm');
fnl.SetFractalOctaves(2);
fnl.SetFrequency(0.04);
TFC.noise.inspect2D('fnl', TFC.noise.fnl2Noise2D(fnl));
TFC.noise.inspect3D('fnl', TFC.noise.fnl2Noise3D(fnl));

const rockType = TFC.noise.cellular2D(0)
    .spread(0.0076)
    .scaled(0, 4)
    .map(v => Math.round(v));
const rockVal = TFC.noise.cellular2D(0)
    .spread(0.0035)
    .scaled(-0x40000000, 0x40000000)
    .rotate(30)
    .map(v => Math.round(v) << 2);

TFC.noise.inspect2D(
    'rock_type',
    rockType
);
TFC.noise.inspect2D(
    'rock_val',
    rockVal
);
TFC.noise.inspect2D(
    'rock_noise',
    rockType.add(rockVal)
);
TFC.noise.inspect2D(
    'forest_noise',
    TFC.noise.openSimplex2D(0)
        .scaled(0, 4)
        .spread(0.005)
        .map(v => Math.round(v))
);
TFC.noise.inspect2D('infinity', TFC.noise.customNoise2D((x, z) => 1 / 0));
TFC.noise.inspect2D('-infinity', TFC.noise.customNoise2D((x, z) => -1 / 0));

const fuzzy = true;

const layeredArea = TFC.worldgen.uniformLayeredArea(413567326);

for (let i = 0 ; i < 3 ; i++) {
    layeredArea.zoom(fuzzy, 19763144126).smooth(79784123632);
}

for (let i = 0 ; i < 6 ; i++) {
    layeredArea.zoom(fuzzy, 451364589723);
}

layeredArea
    .smooth(71214856214)
    .zoom(fuzzy, 854126548632)
    .smooth(145256147896)

TFC.noise.inspect2D('rockLayerArea', (x, z) => layeredArea.getAt(x, z));
TFC.noise.inspect2D('rockLayerAreaType', (x, z) => layeredArea.getAt(x, z) & 0b11);

const cell = TFC.noise.cellular2D(14789614563);

TFC.noise.inspect2D('cell_x', cell.then(c => c.x()));
TFC.noise.inspect2D('cell_y', cell.then(c => c.y()));
TFC.noise.inspect2D('cell_cx', cell.then(c => c.cx()));
TFC.noise.inspect2D('cell_cy', cell.then(c => c.cy()));
TFC.noise.inspect2D('cell_f1', cell.then(c => c.f1()));
TFC.noise.inspect2D('cell_f2', cell.then(c => c.f2()));
TFC.noise.inspect2D('cell', cell);

const cell_3 = TFC.noise.cellular3D(1478563214586);

TFC.noise.inspect3D('cell_f2', (x, y, z) => cell_3.cell(x, y, z).f2());
TFC.noise.inspect3D('cell', cell_3);