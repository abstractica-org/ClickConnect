difference()
{
    linear_extrude(height = 5.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
    {
        scale([10.0, 10.0])
        {
            M22();
        }
    }
    intersection()
    {
        rotate_extrude(angle = 360.0, $fn = 128)
        {
            M113();
        }
        translate([0.0, 0.0, -0.2])
        {
            linear_extrude(height = 10.4, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([5.0, 10.0])
                {
                    M22();
                }
            }
        }
    }
}

module M113()
{
    polygon
    (
        points =
        [
            [0.0, -0.2], 
            [4.1, -0.2], 
            [4.1, 1.4], 
            [3.1, 2.4], 
            [3.1, 2.6], 
            [4.1, 3.6], 
            [4.1, 5.2], 
            [0.0, 5.2]
        ],
        paths =
        [
            [0, 1, 2, 3, 4, 5, 6, 7]
        ]
    );
}

module M22()
{
    polygon
    (
        points =
        [
            [-0.5, -0.5], 
            [0.5, -0.5], 
            [0.5, 0.5], 
            [-0.5, 0.5]
        ],
        paths =
        [
            [0, 1, 2, 3]
        ]
    );
}
