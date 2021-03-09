function dist = dist(north1,east1, north2, east2)

dx = north1 - north2;
dy = east1 - east2;

dist = sqrt(dx .* dx + dy .* dy);

end

