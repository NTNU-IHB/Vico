function data = read_data(path)
% rotation of local vessel coordinates (shape) to xy plot
    if isfile(path) == 1
        data = readmatrix(path);
        %data(1,:) = [];
    else
        data = [];
    end
end
